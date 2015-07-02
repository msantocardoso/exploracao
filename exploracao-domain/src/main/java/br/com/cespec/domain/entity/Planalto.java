package br.com.cespec.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Planalto {

	private int [][] malha;

	private AtomicLong sequencialSonda = new AtomicLong();

	private List<Sonda> sondas;

	public Planalto(int x, int y) {
		this.malha = new int[y][x];
		this.sondas = new ArrayList<>(0);
	}

	public void adicionarSonda(Sonda sonda) {
		if(sonda == null) {
			throw new IllegalArgumentException("N\u00e3o pode adicionar sonda nula na \u00e1rea de explora\u00e7\u00e3o!");
		}

		long id = sequencialSonda.incrementAndGet();
		sonda.setId(id);

		this.sondas.add(sonda);
	}
}
