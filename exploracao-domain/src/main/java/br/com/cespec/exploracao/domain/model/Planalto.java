package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import br.com.cespec.exploracao.domain.transfer.InstrucaoDTO;
import br.com.cespec.exploracao.infra.exception.ColisaoException;
import br.com.cespec.exploracao.infra.exception.ExploracaoRuntimeException;
import br.com.cespec.exploracao.infra.exception.MensagemErro;
import br.com.cespec.exploracao.infra.exception.PontoExploracaoOcupadoException;
import br.com.cespec.exploracao.infra.exception.PosicaoExploracaoInvalidoException;






@Component
@Validated
public class Planalto {

	private AtomicLong sequencialSonda;

	private Map<Long, Sonda> sondas;

	@Autowired
	private Malha malha;

	@PostConstruct
	public void init() {
		inicializar(10, 10);
	}

	public void inicializar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.sondas = new HashMap<>(0);
		this.sequencialSonda = new AtomicLong();
		this.malha.iniciar(x, y);
	}

	public void adicionarSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		if(!this.malha.posicaoDisponivel(sonda.getPosicao())) {
			String mensagem = String.format("A posição [x: %s,y: %s] na qual deseja adicionar a sonda está ocupado, favor definir uma posição para exploração.", sonda.getPosicao().getX(), sonda.getPosicao().getY());

			throw new PontoExploracaoOcupadoException(mensagem);
		}

		long id = sequencialSonda.incrementAndGet();
		sonda.setId(id);

		this.sondas.put(sonda.getId(), sonda);

		this.malha.addSonda(sonda);
	}

	public Sonda adicionarSonda(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y, @NotNull(message="{direcao.notnull}") Direcao direcao) {

		Sonda sonda = novaSonda(x, y, direcao);

		adicionarSonda(sonda);

		return sonda;
	}

	private Sonda novaSonda(int x, int y, Direcao direcao) {
		return new Sonda(x, y, direcao);
	}

	public void removerSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		if(sondas.containsKey(id)) {
			Sonda sonda = sondas.remove(id);

			this.malha.removerSonda(sonda);
		}
	}

	public void removerSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {
		removerSonda(sonda.getId());
	}

	public Sonda buscarSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		return this.sondas.get(id);
	}

	public List<Sonda> getSondas() {

		Collection<Sonda> sondas = this.sondas.values();

		return Collections.unmodifiableList(sondas.stream().collect(Collectors.toList()));
	}

	public Sonda executarInstrucoes(@Min(value=1,message="{sonda.id.invalido}") long id, @NotEmpty String instrucoes) {

		Sonda sonda = buscarSonda(id);

		if(sonda == null) {
			String mensagem = String.format("Nenhuma sonda encontrada com id: [%s]!", id);

			throw new ExploracaoRuntimeException(mensagem);
		}

		simularExecucaoInstrucoes(sonda, instrucoes);

		sonda.executar(instrucoes);

		malha.addSonda(sonda);

		malha.exibirAreaExploracao();

		return sonda;
	}

	private void simularExecucaoInstrucoes(Sonda sonda, String instrucoes) {
		Posicao posicao = sonda.getPosicao();

		Sonda novaSonda = novaSonda(posicao.getX(), posicao.getY(), sonda.getDirecao());

		novaSonda.setId(sonda.getId());

		List<String> erros = new ArrayList<>(0);

		novaSonda.executar(instrucoes, (instrucao, novoEstado) -> {

			try {
				if(malha.haveraColisao(novoEstado)) {
					String mensagem = String.format("Haverá colisão da Sonda [%s] na posição: [x: %s,y: %s] da área de exploração ao executar a instrução: %s", novoEstado.getId(), novoEstado.getPosicao().getX(), novoEstado.getPosicao().getY(), instrucao);

					erros.add(mensagem);
				}
			} catch (PosicaoExploracaoInvalidoException posExc) {
				String mensagem = String.format("A Sonda [%s] sairá da área de exploração [x: %s,y: %s] ao executar a instrução: %s", novoEstado.getId(), novoEstado.getPosicao().getX(), novoEstado.getPosicao().getY(), instrucao);

				erros.add(mensagem);
			}
		});

		if(!erros.isEmpty()) {
			throw new ColisaoException(erros);
		}
	}

	public String getAreaExploracao() {
		return malha.getAreaExploracao();
	}

	public List<Sonda> executarInstrucoes(List<InstrucaoDTO> instrucoes) {

		List<Sonda> sondas = new ArrayList<>(instrucoes.size());

		List<String> erros = new ArrayList<>(0);

		instrucoes.forEach(instrucao -> {
			try {
				Sonda sonda = executarInstrucoes(instrucao.getSondaId(), instrucao.getInstrucoes());

				sondas.add(sonda);
			} catch (ColisaoException exc) {
				List<MensagemErro> errors = exc.getErros();

				errors.forEach(err -> erros.add(err.getMensagem()));
			}
		});

		if(!erros.isEmpty()) {
			throw new ColisaoException(erros);
		}

		return sondas;
	}
}
