package br.com.cespec.domain.entity;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(of= {"id"})
public class Sonda {

	private long id;

	public Sonda(Posicao posicao) {
		this.posicao = posicao;
	}

	@Getter
	private Posicao posicao;

	@Getter
	private Direcao direcao;

	public void executar(Instrucoes instrucao) {
		if(instrucao.equals(Instrucoes.M)) {
			mover();
		} else {
			girar(instrucao);
		}
	}

	public void executar(List<Instrucoes> instrucoes) {
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
