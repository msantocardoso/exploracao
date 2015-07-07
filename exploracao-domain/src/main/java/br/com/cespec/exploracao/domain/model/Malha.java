package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import br.com.cespec.exploracao.domain.repository.SondaRepository;
import br.com.cespec.exploracao.infra.exception.PontoExploracaoOcupadoException;
import br.com.cespec.exploracao.infra.exception.PosicaoExploracaoInvalidoException;


@Component
@Validated
@Slf4j
public class Malha {

	int X, Y;

	private Map<Integer, List<PontoExploracao>> area;

	@Autowired
	private SondaRepository sondaRepository;

	public boolean areaDeExploracaoIniciada()  {
		return area != null;
	}

	public void iniciar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.X = x;
		this.Y = y;
		this.area = new LinkedHashMap<>();

		criarAreaExploracao();
	}

	private void criarAreaExploracao() {
		iterarEixoY(index-> {
			List<PontoExploracao> areaExploracao = criarPontosExploracao();

			area.put(Integer.valueOf(index), areaExploracao);
		});
	}

	private List<PontoExploracao> criarPontosExploracao() {

		List<PontoExploracao> pontos = new ArrayList<>(X);

		iterarEixoX(index-> {
			pontos.add(new PontoExploracao());
		});

		return pontos;
	}

	public boolean posicaoDisponivel(@NotNull(message="{sonda.notnull}") @Valid Posicao posicao) {

		validarPosicao(posicao);

		PontoExploracao pontoExploracao = getPontoExploracao(posicao);

		return pontoExploracao.disponivel();
	}

	public void addSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		Posicao posicao = sonda.getPosicao();

		validarPosicao(posicao);

		if(!posicaoDisponivel(posicao)) {
			String mensagem = String.format("A posição [x: %s,y: %s] na qual deseja adicionar a sonda [%s] está ocupado, favor definir uma posição para exploração.", sonda.getPosicao().getX(), sonda.getPosicao().getY(), sonda.getId());

			throw new PontoExploracaoOcupadoException(mensagem);
		}

		liberarUltimaPosicao(sonda);

		posicionarSonda(sonda);
	}

	private void liberarUltimaPosicao(Sonda sonda) {

		if(sonda.getId() != null) {
			Posicao posicaoAntiga = sondaRepository.buscarUltimaPosicao(sonda.getId());

			liberarPontoExploracao(sonda.getId(), posicaoAntiga);
		}
	}

	private void liberarPontoExploracao(Long idSonda, Posicao posicao) {

		if(idSonda != null && posicao != null) {
			validarPosicao(posicao);

			PontoExploracao pontoExploracao = getPontoExploracao(posicao);

			if(pontoExploracao != null) {
				Sonda sondaAlocada = pontoExploracao.get();

				if(sondaAlocada != null && idSonda.equals(sondaAlocada.getId())) {
					pontoExploracao.setSonda(null);
				}
			}
		}
	}

	public void removerSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		liberarUltimaPosicao(sonda);

		Posicao posicao = sonda.getPosicao();

		liberarPontoExploracao(sonda.getId(), posicao);
	}

	private void posicionarSonda(Sonda sonda) {
		Posicao posicao = sonda.getPosicao();

		if(!haveraColisao(sonda)) {
			sondaRepository.registrarPosicaoSonda(sonda);

			PontoExploracao pontoExploracao = getPontoExploracao(posicao);

			pontoExploracao.setSonda(sonda);
		}
	}

	private PontoExploracao getPontoExploracao(Posicao posicao) {
		validarPosicao(posicao);

		List<PontoExploracao> pontos = area.get(posicao.getY());

		PontoExploracao pontoExploracao = pontos.get(posicao.getX());

		return pontoExploracao;
	}

	private void validarPosicao(Posicao posicao) {
		List<String> mensagens = new ArrayList<>(0);
		if(posicao.getY() < 0 || !area.containsKey(posicao.getY())) {
			mensagens.add("Posição Y informada é inválida!");
		}

		if(posicao.getX() < 0 || posicao.getX() > X) {
			mensagens.add("Posição X informada é inválida!");
		}

		if(!mensagens.isEmpty()) {
			throw new PosicaoExploracaoInvalidoException(mensagens);
		}
	}

	public void exibirAreaExploracao() {

		log.info(getAreaExploracao());
	}

	private void iterarEixoY(Consumer<Integer> posicao) {
		for (int index=Y; index>=0; index--) {
			posicao.accept(index);
		}
	}

	private void iterarEixoX(Consumer<Integer> posicao) {

		for (int index=0; index<=X; index++) {
			posicao.accept(index);
		}
	}


	public boolean haveraColisao(Sonda sonda) {

		boolean colisao = false;

		Posicao posicao = sonda.getPosicao();

		PontoExploracao pontoExploracao = getPontoExploracao(posicao);

		if(!pontoExploracao.disponivel()) {
			Sonda sondaAlocada = pontoExploracao.get();

			if(!sonda.equals(sondaAlocada)) {
				colisao = true;
			}
		}

		return colisao;
	}

	public String getAreaExploracao() {
		String breakLine = System.getProperty("line.separator");

		StringBuilder areaExp = new StringBuilder();
		areaExp.append(breakLine);

		area.forEach((k,v) -> {
			if(areaExp.length()>0) {
				areaExp.append(breakLine);
			}
			if(k<=9)
				areaExp.append(" ");
			areaExp.append(k +" -> " + v);
		});

		areaExp.append(breakLine);
		areaExp.append("   ->  ");

		areaExp.append(getRodape());
		areaExp.append(breakLine);

		return areaExp.toString();
	}

	private String getRodape() {
		StringBuilder rodape = new StringBuilder();
		iterarEixoX(index -> {

			if(rodape.length()>0)
				rodape.append(", ");

			rodape.append("  ");
			rodape.append(index);
			rodape.append("  ");
		});

		return rodape.toString();
	}

	public Sonda getSonda(Posicao posicao) {
		PontoExploracao pontoExploracao = getPontoExploracao(posicao);

		return pontoExploracao.get();
	}

	public void limparAreaExploracao() {
		criarAreaExploracao();
	}
}
