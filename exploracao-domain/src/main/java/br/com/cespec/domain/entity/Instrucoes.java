package br.com.cespec.domain.entity;

public enum Instrucoes {

	L("ESQUERDA"),
	R("DIREITA"),
	M("AVANÇAR");

	Instrucoes(String descricao) {
		this.descricao= descricao;
	}

	private String descricao;

	public String getDescricao() {
		return this.descricao;
	}
}
