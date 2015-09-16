package br.com.cespec.exploracao.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Movimentacao;
import br.com.cespec.exploracao.domain.model.Posicao;

@Component
public class RegrasMovimentacao {

	private Map<Direcao, Movimentacao> regraMovimentacao = new HashMap<>();

	@PostConstruct
	private void init() {
		regraMovimentacao.put(Direcao.N, (posicao) -> { return new Posicao(posicao.getX(), posicao.getY()+1); });
		regraMovimentacao.put(Direcao.E, (posicao) -> { return new Posicao(posicao.getX()+1, posicao.getY()); });
		regraMovimentacao.put(Direcao.S, (posicao) -> { return new Posicao(posicao.getX(), posicao.getY()-1); });
		regraMovimentacao.put(Direcao.W, (posicao) -> { return new Posicao(posicao.getX()-1, posicao.getY()); });
	}

	public Posicao movimentar(Direcao direcao, Posicao posicao) {

		Movimentacao movimentacao = this.regraMovimentacao.get(direcao);

		Posicao nova = movimentacao.movimentar(posicao);

		return nova;
	}
}
