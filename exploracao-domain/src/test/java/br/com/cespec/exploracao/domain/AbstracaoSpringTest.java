package br.com.cespec.exploracao.domain;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Sonda;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ConfiguracaoTeste.class}, locations="classpath*:applicationContextTest.xml")
public abstract class AbstracaoSpringTest {

	protected Sonda novaSonda(int x, int y, Direcao direcao) {
		Sonda sonda = new Sonda(x, y, direcao);

		return sonda;
	}
}
