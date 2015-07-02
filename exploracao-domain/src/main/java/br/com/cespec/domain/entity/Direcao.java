package br.com.cespec.domain.entity;

import java.util.HashMap;
import java.util.Map;

public enum Direcao {

	N("NORTE", "^", "O", "L"),
	S("SUL", "v", "O", "L"),
	L("LESTE", ">", "N", "S"),
	O("OESTE", "<", "S", "N");

	Direcao(String descricao, String simbolo, String esquerda, String direita) {
		this.descricao= descricao;
		this.simbolo  = simbolo;
		this.mapaDirecao.put(Instrucoes.L.name(), Direcao.getDirecao(esquerda));
		this.mapaDirecao.put(Instrucoes.R.name(), Direcao.getDirecao(direita));
	}

	public static Direcao getDirecao(String sigla) {
		Direcao direcao = null;

		for (Direcao dir : values()) {
			if(dir.name().equals(sigla)) {
				direcao = dir;
				break;
			}
		}

		return direcao;
	}

	private Map<String, Direcao> mapaDirecao = new HashMap<>();
	private String descricao;
	private String simbolo;

	public String getSimbolo() {
		return this.simbolo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Direcao getEsquerda() {
		return mapaDirecao.get(Instrucoes.L.name());
	}

	public Direcao getDireita() {
		return mapaDirecao.get(Instrucoes.R.name());
	}
}