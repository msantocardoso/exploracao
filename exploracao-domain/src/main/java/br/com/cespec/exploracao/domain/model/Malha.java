package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import br.com.cespec.exploracao.infra.exception.PontoExploracaoOcupadoException;
import br.com.cespec.exploracao.infra.exception.PosicaoExploracaoInvalidoException;


@Component
@Validated
@Slf4j
public class Malha {

	int X, Y;

	Map<Integer, List<PontoExploracao>> area;

	@PostConstruct
	public void init() {
		area = new HashMap<>();
	}

	public void iniciar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.X = x;
		this.Y = y;

		iterarEixoY(index-> {
			List<PontoExploracao> areaExploracao = criarAreaExploracao();

			area.put(Integer.valueOf(index), areaExploracao);
		});
	}

	public boolean pontoDisponivel(@NotNull(message="{sonda.notnull}") @Valid Posicao posicao) {

		validarPosicao(posicao);

		List<PontoExploracao> pontos = area.get(posicao.getY());

		PontoExploracao pontoExploracao = pontos.get(posicao.getX());

		return pontoExploracao.disponivel();
	}

	public void addSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		Posicao posicao = sonda.getPosicao();

		validarPosicao(posicao);

		if(!pontoDisponivel(posicao)) {
			String mensagem = String.format("A posição [x: %s,y: %s] na qual deseja adicionar a sonda [%s] está ocupado, favor redefinir uma posição para exploração.", sonda.getPosicao().getX(), sonda.getPosicao().getY(), sonda.getId());

			throw new PontoExploracaoOcupadoException(mensagem);
		}

		posicionarSonda(sonda);
	}

	public void removerSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {
		Posicao posicao = sonda.getPosicao();

		PontoExploracao pontoExploracao = getPontoExploracao(posicao);

		Sonda sondaAlocada = pontoExploracao.get();

		if(sonda.equals(sondaAlocada)) {
			pontoExploracao.setSonda(null);
		}
	}

	private void posicionarSonda(Sonda sonda) {
		Posicao posicao = sonda.getPosicao();

		validarPosicao(posicao);

		PontoExploracao pontoExploracao = getPontoExploracao(posicao);

		pontoExploracao.setSonda(sonda);
	}

	private PontoExploracao getPontoExploracao(Posicao posicao) {

		List<PontoExploracao> pontos = area.get(posicao.getY());

		PontoExploracao pontoExploracao = pontos.get(posicao.getX());

		return pontoExploracao;
	}

	private void validarPosicao(Posicao posicao) {
		List<String> mensagens = new ArrayList<>(0);
		if(!area.containsKey(posicao.getY())) {
			mensagens.add("Posição Y informada é inválida!");
		}

		if(posicao.getX() > X) {
			mensagens.add("Posição X informada é inválida!");
		}

		if(!mensagens.isEmpty()) {
			throw new PosicaoExploracaoInvalidoException(mensagens);
		}
	}

	public void exibirAreaExploracao() {

		String breakLine = System.getProperty("line.separator");

		StringBuilder areaExp = new StringBuilder();
		areaExp.append(breakLine);

		iterarEixoY(index -> {
			List<PontoExploracao> areaExploracao = area.get(index);

			if(areaExp.length()>0) {
				areaExp.append(breakLine);
			}

			areaExp.append(index +" -> " + areaExploracao);
		});

		areaExp.append(breakLine);
		areaExp.append("  ->  ");

		areaExp.append(getRodape());
		areaExp.append(breakLine);

		log.info(areaExp.toString());
	}

	private String getRodape() {
		StringBuilder rodape = new StringBuilder();
		iterarEixoX(index -> {

			if(rodape.length()>0)
				rodape.append(", ");

			rodape.append(index);
		});

		return rodape.toString();
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

	private List<PontoExploracao> criarAreaExploracao() {

		List<PontoExploracao> pontos = new ArrayList<>(X);

		iterarEixoY(index-> {
			pontos.add(new PontoExploracao());
		});

		return pontos;
	}
}
