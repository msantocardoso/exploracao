package br.com.cespec.exploracao.domain.model;

import lombok.Data;
import lombok.Getter;

@Data
public class PontoExploracao {

	@Getter
	private Sonda sonda;

	Sonda get(){
		return sonda;
	}

	boolean disponivel() {
		return (sonda == null);
	}

	@Override
	public String toString() {
		return (sonda == null)  ? "X" : "("+sonda.getId()+") "+sonda.getDirecao().getSimbolo();
	}
}
