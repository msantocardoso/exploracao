package br.com.cespec.exploracao.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.cespec.exploracao.domain.AbstracaoSpringTest;

public class PlanaltoTest extends AbstracaoSpringTest {

	@Autowired
	Planalto planalto;

	@Before
	public void inicializar() {
		planalto.inicializar(5, 5);

		planalto.adicionarSonda(0, 0, Direcao.E);
		planalto.adicionarSonda(1, 5, Direcao.N);
	}

	@After
	public void finalizar() {
		planalto = null;
	}

	@Test
	public void deveValidarConfiguracaoDasSondasCriadas() {

		List<Sonda> sondas = planalto.getSondas();

		assertEquals(2, sondas.size());

		Sonda sondaI  = sondas.get(0);
		Sonda sondaII = sondas.get(1);

		assertEquals(Long.valueOf(1), sondaI.getId());
		assertEquals(Long.valueOf(2), sondaII.getId());

		Posicao posicao = sondaI.getPosicao();
		assertEquals(0, posicao.getX());
		assertEquals(0, posicao.getY());

		posicao = sondaII.getPosicao();
		assertEquals(1, posicao.getX());
		assertEquals(5, posicao.getY());

		assertEquals(Direcao.E, sondaI.getDirecao());
		assertEquals(Direcao.N, sondaII.getDirecao());
	}

	@Test
	public void deveRecuperarSondaIPeloIdentificadorEValidarConfiguracoes() {

		Sonda sonda  = planalto.buscarSonda(1);

		assertEquals(Long.valueOf(1), sonda.getId());

		Posicao posicao = sonda.getPosicao();
		assertEquals(0, posicao.getX());
		assertEquals(0, posicao.getY());

		assertEquals(Direcao.E, sonda.getDirecao());
	}

	@Test
	public void deveRecuperarSondaIIPeloIdentificadorEValidarConfiguracoes() {

		Sonda sondaII  = planalto.buscarSonda(2);

		assertEquals(Long.valueOf(2), sondaII.getId());

		Posicao posicao = sondaII.getPosicao();
		assertEquals(1, posicao.getX());
		assertEquals(5, posicao.getY());

		assertEquals(Direcao.N, sondaII.getDirecao());
	}

	@Test
	public void deveAdicionarERemoverSondaPeloId() {
		Sonda sonda = planalto.adicionarSonda(4, 3, Direcao.S);

		planalto.removerSonda(sonda.getId());

		sonda = planalto.buscarSonda(sonda.getId());

		assertNull(sonda);
	}

	@Test
	public void deveAdicionarERemoverSondaPassandoObjeto() {
		Sonda sonda = planalto.adicionarSonda(0, 2, Direcao.W);

		planalto.removerSonda(sonda);

		sonda = planalto.buscarSonda(sonda.getId());

		assertNull(sonda);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRecuperarSondaPassandoIdNegativo() {
		planalto.buscarSonda(-1);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRecuperarSondaPassandoIdInvalido() {
		planalto.buscarSonda(0);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarAdicionarSondaNula() {
		planalto.adicionarSonda(null);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarAdicionarSondaComValoresInvalidos() {
		planalto.adicionarSonda(new Sonda(-1,-10, null));
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarAdicionarSondaComCoordenadasNegativasEDirecaoNula() {
		planalto.adicionarSonda(-1, -5, null);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoIdNegativo() {
		planalto.removerSonda(-1);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoIdInvalido() {
		planalto.removerSonda(0);
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoObjetoNulo() {
		planalto.removerSonda(null);
	}

	@Test(expected=UnsupportedOperationException.class)
	public void deveLancarUnsupportedOperationExceptionAoTentarLimparAListaDeSondas() {

		List<Sonda> sondas = planalto.getSondas();

		sondas.clear();
	}

	@Test(expected=UnsupportedOperationException.class)
	public void deveLancarUnsupportedOperationExceptionAoTentarAdicionarNovaSondasAtravesDaListaDeSondas() {

		List<Sonda> sondas = planalto.getSondas();

		sondas.add(null);
	}
}
