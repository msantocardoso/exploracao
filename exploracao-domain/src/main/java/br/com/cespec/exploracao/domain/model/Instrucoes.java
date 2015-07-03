package br.com.cespec.exploracao.domain.model;

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
