package br.com.cespec.exploracao.domain.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(of= {"id"})
public class Sonda {

	private long id;

	@NotNull
	@Getter
	private Posicao posicao;

	@NotNull
	@Getter
	private Direcao direcao;

	public Sonda(Posicao posicao, Direcao direcao) {
		this.posicao = posicao;
		this.direcao = direcao;
	}

	public void executar(Instrucoes instrucao) {
		if(instrucao == null) {
			throw new IllegalArgumentException("Instrução não pode ser nula!");
		}

		if(instrucao.equals(Instrucoes.M)) {
			mover();
		} else {
			girar(instrucao);
		}
	}

	public void executar(List<Instrucoes> instrucoes) {
		if(instrucoes == null) {
			throw new IllegalArgumentException("As instruções não pode ser nula!");
		}

		instrucoes.forEach((instrucao) -> {
			executar(instrucao);
		});
	}

	private void mover() {
		posicao.mover(direcao);
	}

	private void girar(Instrucoes instrucao) {
		if(instrucao.equals(Instrucoes.L)) {
			direcao = direcao.getEsquerda();
		} else if(instrucao.equals(Instrucoes.R)) {
			direcao = direcao.getDireita();
		}
	}
}
