package br.com.cespec.exploracao.domain;

import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ConfiguracaoTeste.class}, locations="classpath*:applicationContextTest.xml")
public abstract class AbstracaoSpringTest {

}
