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

import br.com.cespec.exploracao.domain.repository.SondaRepository;
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
	private SondaRepository sondaRepository;

	@Autowired
	private Malha malha;

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

		this.sondaRepository.adicionarSonda(sonda);

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

		Sonda sonda = sondaRepository.buscarSonda(id);

		if(sonda == null) {
			String mensagem = String.format("Nenhuma sonda encontrada com id: [%s]!", id);

			throw new ExploracaoRuntimeException(mensagem);
		}

		sondaRepository.removerSonda(id);

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

		Sonda sonda = sondaRepository.buscarSonda(id);

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

		Sonda novaSonda = Sonda.novaSonda(posicao.getX(), posicao.getY(), sonda.getDirecao());

		novaSonda.setId(sonda.getId());

		List<String> erros = new ArrayList<>(0);

		novaSonda.executar(instrucoes, (indiceInstrucao, novoEstado) -> {

			String instrucao = Instrucoes.destacadarInstrucao(instrucoes, indiceInstrucao);
			try {
				if(Instrucoes.isInstrucaoMovimentacao(instrucoes, indiceInstrucao) && malha.haveraColisao(novoEstado)) {

					Sonda sondaColisao = malha.getSonda(novoEstado.getPosicao());

					String colisao = (sondaColisao == null) ? "" : String.format("com a sonda [%s]", sondaColisao.getId());

					String mensagem = String.format("Haverá colisão da Sonda [%s] %s na posição: [x: %s,y: %s] da área de exploração ao executar a instrução: %s", novoEstado.getId(), colisao, novoEstado.getPosicao().getX(), novoEstado.getPosicao().getY(), instrucao);

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
