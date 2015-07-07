package br.com.cespec.exploracao.domain.repository;

import java.util.List;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Posicao;
import br.com.cespec.exploracao.domain.model.Sonda;

public interface SondaRepository {

	void adicionarSonda(Sonda sonda);

	Sonda adicionarSonda(int x, int y, Direcao direcao);

	Sonda removerSonda(Long id);

	void removerSonda(Sonda sonda);

	void removerTodasAsSondas();

	Sonda buscarSonda(Long id);

	List<Sonda> buscarTodas();

	Posicao buscarUltimaPosicao(Long id);

	void registrarPosicaoSonda(Sonda sonda);
}
