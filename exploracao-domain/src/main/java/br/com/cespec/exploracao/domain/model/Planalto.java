package br.com.cespec.exploracao.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class Planalto {

	private AtomicLong sequencialSonda;

	private Map<Long, Sonda> sondas;

	@Autowired
	private Malha malha;

	@PostConstruct
	public void init() {
		inicializar(10, 10);
	}

	public void inicializar(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y) {
		this.sondas = new HashMap<>(0);
		this.sequencialSonda = new AtomicLong();
		this.malha.iniciar(x, y);
	}

	public void adicionarSonda(@NotNull(message="{sonda.notnull}") @Valid Sonda sonda) {

		long id = sequencialSonda.incrementAndGet();
		sonda.setId(id);

		this.sondas.put(sonda.getId(), sonda);

		this.malha.addSonda(sonda);
	}

	public Sonda adicionarSonda(@Min(value=0,message="{coordenada.negativa}") int x, @Min(value=0,message="{coordenada.negativa}") int y, @NotNull(message="{direcao.notnull}") Direcao direcao) {

		Sonda sonda = new Sonda(x, y, direcao);

		adicionarSonda(sonda);

		return sonda;
	}

	public void removerSonda(@Min(value=1,message="{sonda.id.invalido}") long id) {
		if(sondas.containsKey(id)) {
			Sonda sonda = sondas.remove(id);

			this.malha.removerSonda(sonda);
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
