package br.com.cespec.exploracao.domain.model;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.cespec.exploracao.domain.AbstracaoSpringTest;
import br.com.cespec.exploracao.domain.RegrasMovimentacao;
import br.com.cespec.exploracao.domain.RegrasRotacao;
import br.com.cespec.exploracao.infra.exception.InstrucaoInvalidaException;

public class SondaTest extends AbstracaoSpringTest {

	Sonda sonda;

	@Autowired
	RegrasRotacao rotacao;

	@Autowired
	RegrasMovimentacao movimentacao;

	@Before
	public void setUp() {
		sonda = this.novaSonda(0, 0, Direcao.E);
	}

	@After
	public void tearDown() {
		sonda = null;
	}

	@Test
	public void deveMovimentarSondaConformeInstrucaoDeMovimentacao() {

		sonda.executar("M", movimentacao, rotacao);

		Posicao posicao = sonda.getPosicao();

		assertEquals(1, posicao.getX());
		assertEquals(0, posicao.getY());

		Direcao direcao = sonda.getDirecao();

		assertEquals(Direcao.E, direcao);
	}

	@Test
	public void deveVirarSondaParaEsquerda() {

		sonda.executar("L", movimentacao, rotacao);

		Posicao posicao = sonda.getPosicao();

		assertEquals(0, posicao.getX());
		assertEquals(0, posicao.getY());

		Direcao direcao = sonda.getDirecao();

		assertEquals(Direcao.N, direcao);
	}

	@Test
	public void deveVirarSondaParaDireita() {

		sonda.executar("R", movimentacao, rotacao);

		Posicao posicao = sonda.getPosicao();

		assertEquals(0, posicao.getX());
		assertEquals(0, posicao.getY());

		Direcao direcao = sonda.getDirecao();

		assertEquals(Direcao.S, direcao);
	}

	@Test
	public void deveExecutarSequenciaDeInstrucoesDeExemploI() {

		Sonda sd = this.novaSonda(1, 2, Direcao.N);

		sd.executar("LMLMLMLMM", movimentacao, rotacao);

		Posicao posicao = sd.getPosicao();

		assertEquals(1, posicao.getX());
		assertEquals(3, posicao.getY());

		Direcao direcao = sd.getDirecao();

		assertEquals(Direcao.N, direcao);
	}

	@Test
	public void deveExecutarSequenciaDeInstrucoesDoExemploII() {

		Sonda sd = new Sonda(3, 3, Direcao.E);

		sd.executar("MMRMMRMRRM", movimentacao, rotacao);

		Posicao posicao = sd.getPosicao();

		assertEquals(5, posicao.getX());
		assertEquals(1, posicao.getY());

		Direcao direcao = sd.getDirecao();

		assertEquals(Direcao.E, direcao);
	}

	@Test(expected = InstrucaoInvalidaException.class)
	public void deveLancarExceptionAoPassarUmaInstrucaoNulaParaSonda() {
		sonda.executar(null, movimentacao, rotacao);
	}

	@Test(expected = InstrucaoInvalidaException.class)
	public void deveLancarExceptionAoPassarUmaInstrucaoVaziaParaSonda() {
		sonda.executar("", movimentacao, rotacao);
	}

	@Test(expected = InstrucaoInvalidaException.class)
	public void deveLancarExceptionAoPassarUmaInstrucaoInvalidaParaSonda() {
		sonda.executar("A", movimentacao, rotacao);
	}
}