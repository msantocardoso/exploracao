package br.com.cespec.exploracao.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Instrucoes;
import br.com.cespec.exploracao.domain.model.Rotacao;

@Component
public class RegrasRotacao {

	private Map<Instrucoes, Rotacao> regras = new HashMap<>();

	@PostConstruct
	private void init() {
		regras.put(Instrucoes.L, (direcao) -> { return direcao.getEsquerda(); });
		regras.put(Instrucoes.R, (direcao) -> { return direcao.getDireita(); });
	}

	public Direcao girar(Instrucoes instrucao, Direcao direcao) {

		Rotacao rotacao = regras.get(instrucao);

		Direcao novaDirecao = rotacao.girar(direcao);

		return novaDirecao;
	}
}
