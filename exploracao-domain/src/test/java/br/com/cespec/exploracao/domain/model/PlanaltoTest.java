package br.com.cespec.exploracao.domain.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.cespec.exploracao.domain.AbstracaoSpringTest;
import br.com.cespec.exploracao.domain.transfer.InstrucaoDTO;
import br.com.cespec.exploracao.infra.exception.ColisaoException;
import br.com.cespec.exploracao.infra.exception.MensagemErro;

public class PlanaltoTest extends AbstracaoSpringTest {

	@Autowired
	Planalto planalto;

	@Before
	public void inicializar() {
		planalto.inicializar(5, 5);
	}

	@After
	public void finalizar() {
		planalto.limparAreaExploracao();
	}

	@Test
	public void deveLancarDuasMensagensDeErroDuranteAExecucaoDasInstrucoes() {
		planalto.adicionarSonda(1, 2, Direcao.N);
		planalto.adicionarSonda(1, 3, Direcao.N);

		try {
			planalto.executarInstrucoes(Long.valueOf(1), "LMLMLMLMM");
		} catch (ColisaoException e) {
			List<MensagemErro> erros = e.getErros();

			assertEquals(1, erros.size());
		}
	}

	@Test
	public void deveLancarDuasMensagensDeErroDuranteAExecucaoDasInstrucoesAsSondas() {
		planalto.adicionarSonda(1, 2, Direcao.N);
		planalto.adicionarSonda(3, 3, Direcao.E);
		planalto.adicionarSonda(1, 3, Direcao.N);
		planalto.adicionarSonda(5, 1, Direcao.E);

		List<InstrucaoDTO> instrucoes = new ArrayList<>(2);

		instrucoes.add(new InstrucaoDTO(Long.valueOf(1),"LMLMLMLMM"));
		instrucoes.add(new InstrucaoDTO(Long.valueOf(2),"MMRMMRMRRM"));
		try {
			planalto.executarInstrucoes(instrucoes);
		} catch (ColisaoException e) {
			List<MensagemErro> erros = e.getErros();

			assertEquals(3, erros.size());
		}
	}

	@Test(expected = ColisaoException.class)
	public void deveLancarColisaoExceptionSeDuranteAExecucaoDasInstrucoesASondaInvadirAreaDeExplocaoUsadaPorOutraSonda() {
		planalto.adicionarSonda(1, 2, Direcao.N);
		planalto.adicionarSonda(1, 3, Direcao.N);

		planalto.executarInstrucoes(Long.valueOf(1), "LMLMLMLMM");
	}

	@Test(expected = ColisaoException.class)
	public void deveLancarColisaoExceptionSeDuranteAExecucaoDasInstrucoesAsSondasInvadiremAreaDeExplocaoUsadaPorOutrasSondas() {
		planalto.adicionarSonda(1, 2, Direcao.N);
		planalto.adicionarSonda(3, 3, Direcao.E);
		planalto.adicionarSonda(1, 3, Direcao.N);
		planalto.adicionarSonda(5, 1, Direcao.E);

		List<InstrucaoDTO> instrucoes = new ArrayList<>(2);

		instrucoes.add(new InstrucaoDTO(Long.valueOf(1),"LMLMLMLMM"));
		instrucoes.add(new InstrucaoDTO(Long.valueOf(2),"MMRMMRMRRM"));

		planalto.executarInstrucoes(instrucoes);
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
		planalto.removerSonda(Long.valueOf(-1));
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoIdInvalido() {
		planalto.removerSonda(Long.valueOf(-10));
	}

	@Test(expected=ConstraintViolationException.class)
	public void deveLancarConstraintViolationExceptionAoTentarRemoverSondaPassandoObjetoNulo() {
		planalto.removerSonda(null);
	}
}
