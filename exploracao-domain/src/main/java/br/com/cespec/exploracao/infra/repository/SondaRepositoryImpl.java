package br.com.cespec.exploracao.infra.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Posicao;
import br.com.cespec.exploracao.domain.model.Sonda;
import br.com.cespec.exploracao.domain.repository.SondaRepository;

@Repository
public class SondaRepositoryImpl implements SondaRepository {

	private Map<Long, Sonda> sondas;

	private Map<Long, Posicao> ultimaPosicao;

	private AtomicLong sequencialSonda;

	@PostConstruct
	public void init() {
		this.sondas = new HashMap<>(0);
		this.sequencialSonda = new AtomicLong();
		this.ultimaPosicao = new HashMap<>();
	}

	@Override
	public void adicionarSonda(Sonda sonda) {
		Long id = sequencialSonda.incrementAndGet();
		sonda.setId(id);

		this.sondas.put(sonda.getId(), sonda);
	}

	@Override
	public Sonda adicionarSonda(int x, int y, Direcao direcao) {
		Sonda novaSonda = Sonda.novaSonda(x, y, direcao);

		adicionarSonda(novaSonda);

		return novaSonda;
	}

	@Override
	public Sonda removerSonda(Long id) {
		Sonda sonda = null;
		if(sondas.containsKey(id)) {
			sonda = sondas.remove(id);
		}

		return sonda;
	}

	@Override
	public void removerSonda(Sonda sonda) {
		removerSonda(sonda.getId());
	}

	@Override
	public Sonda buscarSonda(Long id) {
		Sonda sonda = null;

		if(this.sondas.containsKey(id)) {
			sonda = this.sondas.get(id);
		}

		return sonda;
	}

	@Override
	public List<Sonda> buscarTodas() {

		Collection<Sonda> sondas = this.sondas.values();

		return Collections.unmodifiableList(sondas.stream().collect(Collectors.toList()));
	}

	@Override
	public Posicao buscarUltimaPosicao(Long id) {
		Posicao posicaoAntiga = null;

		if(sondas.containsKey(id)) {
			posicaoAntiga = ultimaPosicao.get(id);
		}

		return posicaoAntiga;
	}

	@Override
	public void registrarPosicaoSonda(Sonda sonda) {
		Posicao posicao = sonda.getPosicao();

		ultimaPosicao.put(sonda.getId(), new Posicao(posicao.getX(), posicao.getY()));
	}

	@Override
	public void removerTodasAsSondas() {
		this.sondas.clear();
		this.ultimaPosicao.clear();
		this.sequencialSonda = new AtomicLong();
	}
}
