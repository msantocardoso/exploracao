package br.com.cespec.exploracao.domain.model;

@FunctionalInterface
public interface Movimentacao {

	abstract Posicao movimentar(Posicao posicao);
}
