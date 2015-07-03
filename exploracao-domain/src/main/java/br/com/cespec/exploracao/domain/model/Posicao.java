package br.com.cespec.exploracao.domain.model;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Posicao {

	@Min(value=0)
	private int x;

	@Min(value=0)
	private int y;

	public void mover(Direcao direcao) {
		if(direcao.equals(Direcao.N)) {
			y++;
		} else if(direcao.equals(Direcao.E)) {
			x++;
		} else if(direcao.equals(Direcao.S)) {
			y--;
		} else if(direcao.equals(Direcao.W)) {
			x--;
		}
	}
}
