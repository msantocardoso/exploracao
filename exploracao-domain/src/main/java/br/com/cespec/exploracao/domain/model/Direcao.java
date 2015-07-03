package br.com.cespec.exploracao.domain.model;

import java.util.HashMap;
import java.util.Map;

public enum Direcao {

	N("NORTH", "^", "W", "E"),
	S("SOUTH", "v", "W", "E"),
	E("EAST", ">", "N", "S"),
	W("WEST", "<", "S", "N");

	private Map<String, String> mapaDirecao = new HashMap<>();
	private String descricao;
	private String simbolo;

	Direcao(String descricao, String simbolo, String esquerda, String direita) {
		this.descricao= descricao;
		this.simbolo  = simbolo;
		this.mapaDirecao.put(Instrucoes.L.name(), esquerda);
		this.mapaDirecao.put(Instrucoes.R.name(), direita);
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

	public String getSimbolo() {
		return this.simbolo;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Direcao getEsquerda() {
		String sigla = mapaDirecao.get(Instrucoes.L.name());

		return Direcao.getDirecao(sigla);
	}

	public Direcao getDireita() {

		String sigla = mapaDirecao.get(Instrucoes.R.name());

		return Direcao.getDirecao(sigla);
	}
}