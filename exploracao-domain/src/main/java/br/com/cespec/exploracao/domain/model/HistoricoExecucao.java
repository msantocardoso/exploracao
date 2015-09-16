package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class HistoricoExecucao {

	private Sonda sonda;

	private List<Sonda> histMovimentacao = new ArrayList<>();

	public void registrar(Sonda sonda) {
		this.histMovimentacao.add(sonda);
	}
}
