package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import br.com.cespec.exploracao.domain.RegrasMovimentacao;
import br.com.cespec.exploracao.domain.RegrasRotacao;
import br.com.cespec.exploracao.domain.repository.Sondas;
import br.com.cespec.exploracao.domain.transfer.InstrucaoDTO;
import br.com.cespec.exploracao.infra.exception.ColisaoException;
import br.com.cespec.exploracao.infra.exception.ExploracaoRuntimeException;
import br.com.cespec.exploracao.infra.exception.MensagemErro;
import br.com.cespec.exploracao.infra.exception.PontoExploracaoOcupadoException;
import br.com.cespec.exploracao.infra.exception.PosicaoExploracaoInvalidoException;

@Component
@Validated
public class Planalto {

	@Autowired
	private Sondas sondaRepository;

	@Autowired
	private Malha malha;

	@Autowired
	private RegrasMovimentacao regrasMovimentacao;

	@Autowired
	private RegrasRotacao regrasRotacao;

	public void inicializar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.malha.iniciar(x, y);
	}

	private void validarAreaExploracao() {
		if(!this.malha.areaDeExploracaoIniciada()) {
			throw new ExploracaoRuntimeException("Favor iniciar a area de exploracacao!");
		}
	}

	public void adicionarSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		validarAreaExploracao();

		if(!this.malha.posicaoDisponivel(sonda.getPosicao())) {
			String mensagem = String.format("A posição [x: %s,y: %s] na qual deseja adicionar a sonda está ocupado, favor definir uma posição para exploração.", sonda.getPosicao().getX(), sonda.getPosicao().getY());

			throw new PontoExploracaoOcupadoException(mensagem);
		}

		this.sondaRepository.adicionar(sonda);

		this.malha.addSonda(sonda);
	}

	public Sonda adicionarSonda(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y, @NotNull(message="{direcao.notnull}") Direcao direcao) {

		validarAreaExploracao();

		Sonda sonda = Sonda.novaSonda(x, y, direcao);

		adicionarSonda(sonda);

		return sonda;
	}

	public void removerSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		validarAreaExploracao();

		Sonda sonda = sondaRepository.buscar(id);

		if(sonda == null) {
			String mensagem = String.format("Nenhuma sonda encontrada com id: [%s]!", id);

			throw new ExploracaoRuntimeException(mensagem);
		}

		sondaRepository.remover(id);

		if(sonda != null) {
			this.malha.removerSonda(sonda);
		}
	}

	public void removerSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {
		removerSonda(sonda.getId());
	}

	public String getAreaExploracao() {
		validarAreaExploracao();

		return malha.getAreaExploracao();
	}

	public Sonda executarInstrucoes(@Min(value=1,message="{sonda.id.invalido}") Long id, @NotEmpty String instrucoes) {
		validarAreaExploracao();

		Sonda sonda = sondaRepository.buscar(id);

		if(sonda == null) {
			String mensagem = String.format("Nenhuma sonda encontrada com id: [%s]!", id);

			throw new ExploracaoRuntimeException(mensagem);
		}

		HistoricoExecucao histExecucao = sonda.executar(instrucoes, regrasMovimentacao, regrasRotacao);

		validarExecucao(instrucoes, histExecucao);

		malha.addSonda(sonda);

		malha.exibirAreaExploracao();

		return sonda;
	}

	private void validarExecucao(String instrucoes, HistoricoExecucao historicoExecucao) {

		List<String> erros = new ArrayList<>(0);

		List<Sonda> histMovimentacao = historicoExecucao.getHistMovimentacao();

		int indiceInstrucao = 0;
		for (Sonda sonda : histMovimentacao) {

			String instrucao = Instrucoes.destacarInstrucao(instrucoes, indiceInstrucao);
			try {
				if(Instrucoes.isInstrucaoMovimentacao(instrucoes, indiceInstrucao) && malha.haveraColisao(sonda)) {

					Sonda sondaColisao = malha.getSonda(sonda.getPosicao());

					String colisao = (sondaColisao == null) ? "" : String.format("com a sonda [%s]", sondaColisao.getId());

					String mensagem = String.format("Haverá colisão da Sonda [%s] %s na posição: [x: %s,y: %s] da área de exploração ao executar a instrução: %s", sonda.getId(), colisao, sonda.getPosicao().getX(), sonda.getPosicao().getY(), instrucao);

					erros.add(mensagem);
				}
				indiceInstrucao++;
			} catch (PosicaoExploracaoInvalidoException posExc) {
				String mensagem = String.format("A Sonda [%s] sairá da área de exploração [x: %s,y: %s] ao executar a instrução: %s", sonda.getId(), sonda.getPosicao().getX(), sonda.getPosicao().getY(), instrucao);

				erros.add(mensagem);
			}
		}

		if(!erros.isEmpty()) {
			throw new ColisaoException(erros);
		}
	}

	public List<Sonda> executarInstrucoes(List<InstrucaoDTO> instrucoes) {

		validarAreaExploracao();

		List<Sonda> sondas = new ArrayList<>(instrucoes.size());

		List<String> erros = new ArrayList<>(0);

		instrucoes.forEach(instrucao -> {
			try {
				Sonda sonda = executarInstrucoes(instrucao.getSondaId(), instrucao.getInstrucoes());

				sondas.add(sonda);
			} catch (ColisaoException exc) {
				List<MensagemErro> errors = exc.getErros();

				errors.forEach(err -> erros.add(err.getMensagem()));
			} catch(ExploracaoRuntimeException ec) {
				List<MensagemErro> errors = ec.getErros();

				errors.forEach(err -> erros.add(err.getMensagem()));
			}
		});

		if(!erros.isEmpty()) {
			throw new ColisaoException(erros);
		}

		return sondas;
	}

	public void limparAreaExploracao() {
		this.malha.limparAreaExploracao();
		this.sondaRepository.removerTodasAsSondas();
	}
}
