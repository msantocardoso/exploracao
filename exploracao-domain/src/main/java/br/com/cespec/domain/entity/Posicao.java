package br.com.cespec.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Posicao {

	private int x;
	private int y;

	public void mover(Direcao direcao) {
		if(direcao.equals(Direcao.N)) {
			y++;
		} else if(direcao.equals(Direcao.L)) {
			x++;
		} else if(direcao.equals(Direcao.S)) {
			y--;
		} else if(direcao.equals(Direcao.O)) {
			x--;
		}
	}
}
