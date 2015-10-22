package br.com.cespec.exploracao.domain.repository;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Posicao;
import br.com.cespec.exploracao.domain.model.Sonda;

public interface Sondas {

	void adicionar(Sonda sonda);

	Sonda adicionar(int x, int y, Direcao direcao);

	Sonda remover(Long id);

	void remover(Sonda sonda);

	void removerTodasAsSondas();

	Sonda buscar(Long id);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	List<Sonda> buscarTodas();

	Posicao buscarUltimaPosicao(Long id);

	void registrarPosicaoSonda(Sonda sonda);
}
