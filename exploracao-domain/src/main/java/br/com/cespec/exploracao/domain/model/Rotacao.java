package br.com.cespec.exploracao.domain.model;

@FunctionalInterface
public interface Rotacao {

	abstract Direcao girar(Direcao direcao);
}
