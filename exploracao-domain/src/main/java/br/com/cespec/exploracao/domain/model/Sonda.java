package br.com.cespec.exploracao.domain.model;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.function.BiConsumer;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import br.com.cespec.exploracao.domain.RegrasMovimentacao;
import br.com.cespec.exploracao.domain.RegrasRotacao;
import br.com.cespec.exploracao.infra.exception.InstrucaoInvalidaException;

@Data
@EqualsAndHashCode(of= {"id"})
public class Sonda {

	private Long id;

	@NotNull
	@Getter
	private Posicao posicao;

	@NotNull
	@Getter
	private Direcao direcao;

	public Sonda(int x, int y, Direcao direcao) {
		this.posicao = new Posicao(x, y);
		this.direcao = direcao;
	}

	public static Sonda novaSonda(int x, int y, Direcao direcao) {
		return new Sonda(x, y, direcao);
	}

	public HistoricoExecucao executar(String instrucoes, RegrasMovimentacao regrasMovimentacao, RegrasRotacao regrasRotacao) {

		if(isBlank(instrucoes)) {
			throw new InstrucaoInvalidaException("As instruções não pode ser nula!");
		}

		HistoricoExecucao hist = new HistoricoExecucao();

		List<Instrucoes> listInstrucoes = Instrucoes.getInstrucoes(instrucoes);

		executar(listInstrucoes, regrasMovimentacao, regrasRotacao, (index, sonda) -> {

			hist.registrar(sonda);

			this.setDirecao(sonda.getDirecao());
			this.setPosicao(sonda.getPosicao());

			if(index == (listInstrucoes.size()-1)) {
				hist.setSonda(sonda);
			}
		});

		return hist;
	}

	private void executar(List<Instrucoes> instrucoes, RegrasMovimentacao regrasMovimentacao, RegrasRotacao regrasRotacao, BiConsumer<Integer,Sonda> transicao) {
		if(instrucoes == null) {
			throw new InstrucaoInvalidaException("As instruções não pode ser nula!");
		}

		int indice = 0;
		Sonda sonda = null;
		for(Instrucoes instrucao : instrucoes) {

			if(instrucao.equals(Instrucoes.M)) {
				Posicao novaPosicao = regrasMovimentacao.movimentar(direcao, posicao);

				sonda = new Sonda(novaPosicao.getX(), novaPosicao.getY(), direcao);
			} else {
				Direcao novaDirecao = regrasRotacao.girar(instrucao, direcao);

				sonda = new Sonda(posicao.getX(), posicao.getY(), novaDirecao);
			}

			if(transicao != null) {
			    sonda.setId(this.getId());
				transicao.accept(indice++, sonda);
			}
		}
	}
}