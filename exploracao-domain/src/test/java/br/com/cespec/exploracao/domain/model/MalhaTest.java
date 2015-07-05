package br.com.cespec.exploracao.domain.model;

import static org.junit.Assert.assertEquals;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.cespec.exploracao.domain.AbstracaoSpringTest;
import br.com.cespec.exploracao.infra.exception.PontoExploracaoOcupadoException;
import br.com.cespec.exploracao.infra.exception.PosicaoExploracaoInvalidoException;

public class MalhaTest extends AbstracaoSpringTest {

	@Autowired
	Malha malha;

	@Before
	public void setUp() {
		malha.iniciar(5,5);
	}

	@After
	public void tearDown() {
		malha = null;
	}

	@Test
	public void deveAdicionarSondaAAreaDeExploracao() {
		Sonda sonda = novaSonda(0,0, Direcao.W);

		sonda.setId(1);
		malha.addSonda(sonda);

		malha.exibirAreaExploracao();
	}

	@Test
	public void deveAdicionarSondaAAreaDeExploracaoEIndicarQuePosicaoNaoEstaDisponivel() {
		Sonda sonda = novaSonda(0,0, Direcao.W);

		sonda.setId(1);
		malha.addSonda(sonda);

		assertEquals(false, malha.pontoDisponivel(sonda.getPosicao()));
	}

	@Test(expected=PosicaoExploracaoInvalidoException.class)
	public void deveLancarErroAoTentarAdicionarSondaEmUmaAreaInvalida() {

		Sonda sonda = novaSonda(10,6, Direcao.W);

		sonda.setId(1);
		malha.addSonda(sonda);
	}

	@Test(expected=PontoExploracaoOcupadoException.class)
	public void deveLancarErroAoTentarAdicionarSondaEmUmaAreaOcupada() {

		Sonda sonda = novaSonda(0,0, Direcao.E);
		sonda.setId(1);
		malha.addSonda(sonda);

		Sonda sondaI = novaSonda(0,0, Direcao.N);
		sondaI.setId(2);
		malha.addSonda(sondaI);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarAdicionarSondaNula() {
		malha.addSonda(null);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarAdicionarSondaComValoresInvalidos() {
		malha.addSonda(new Sonda(-1,-10, null));
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoValoresInvalidos() {
		Sonda sonda = novaSonda(-1,-10, null);

		malha.removerSonda(sonda);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoObjetoNulo() {
		malha.removerSonda(null);
	}
}
