package br.com.cespec.exploracao.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class Planalto {

	private int [][] malha;

	private AtomicLong sequencialSonda;

	private Map<Long, Sonda> sondas;

	public Planalto() {
		inicializar(10, 10);
	}

	public Planalto(int x, int y) {
		inicializar(x, y);
	}

	public void inicializar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.malha = new int[y][x];
		this.sondas = new HashMap<>(0);
		this.sequencialSonda = new AtomicLong();
	}

	public void adicionarSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		long id = sequencialSonda.incrementAndGet();
		sonda.setId(id);

		this.sondas.put(sonda.getId(), sonda);
	}

	public Sonda adicionarSonda(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y, @NotNull(message="{direcao.notnull}") Direcao direcao) {

		Sonda sonda = new Sonda(new Posicao(x, y), direcao);

		adicionarSonda(sonda);

		return sonda;
	}

	public void removerSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		if(sondas.containsKey(id)) {
			sondas.remove(id);
		}
	}

	public void removerSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {
		removerSonda(sonda.getId());
	}

	public Sonda buscarSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		return this.sondas.get(id);
	}

	public List<Sonda> getSondas() {

		Collection<Sonda> sondas = this.sondas.values();

		return Collections.unmodifiableList(sondas.stream().collect(Collectors.toList()));
	}
}
