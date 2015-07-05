package br.com.cespec.exploracao.domain.model;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class Posicao {

	@Min(value=0)
	private int x;

	@Min(value=0)
	private int y;

	private Map<Direcao, Movimentacao> regraMovimentacao = new HashMap<>();

	public Posicao(int x, int y) {
		this.x = x;
		this.y = y;

		regraMovimentacao.put(Direcao.N, () ->  this.y++);
		regraMovimentacao.put(Direcao.E, () ->  this.x++);
		regraMovimentacao.put(Direcao.S, () ->  this.y--);
		regraMovimentacao.put(Direcao.W, () ->  this.x--);
	}

	public void mover(Direcao direcao) {

		Movimentacao mov = regraMovimentacao.get(direcao);

		mov.movimentar();
	}
}
