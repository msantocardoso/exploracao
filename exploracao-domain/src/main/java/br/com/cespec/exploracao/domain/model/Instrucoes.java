package br.com.cespec.exploracao.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import br.com.cespec.exploracao.infra.exception.InstrucaoInvalidaException;

public enum Instrucoes {

	L("ESQUERDA"),
	R("DIREITA"),
	M("AVANÇAR");

	Instrucoes(String descricao) {
		this.descricao= descricao;
	}

	private String descricao;

	public String getDescricao() {
		return this.descricao;
	}

	public static List<Instrucoes> getInstrucoes(String instrucoes) {

		List<String> erros = new ArrayList<>(0);

		if(instrucoes == null || instrucoes.isEmpty()) {
			throw new InstrucaoInvalidaException("Instru\u00e7\u00e3o \u00e9 obrigat\u00f3rio, informe uma instru\u00e7\u00e3o v\u00e1lida!");
		}

		List<Instrucoes> listaInstrucoes = new ArrayList<>(instrucoes.length());

		buscarInstrucoes(instrucoes, (charInst, instrucao) -> {

			if(instrucao == null) {
				erros.add(String.format("Instru\u00e7\u00e3o inv\u00e1lida %s ", charInst));
			} else {
				listaInstrucoes.add(instrucao);
			}
		});

		if(!erros.isEmpty()) {
			throw new InstrucaoInvalidaException(erros);
		}

		return listaInstrucoes;
	}

	private static void buscarInstrucoes(String instrucoes, BiConsumer<Character, Instrucoes> resultado) {

		for(int index=0; index<instrucoes.length(); index++) {

			char inst = instrucoes.charAt(index);

			Instrucoes instrucao = getInstrucao(String.valueOf(inst));

			resultado.accept(Character.valueOf(inst), instrucao);
		}
	}

	public static Instrucoes getInstrucao(String instrucao) {
		Instrucoes instr = null;

		for (Instrucoes item : values()) {
			if(item.name().equals(instrucao)) {
				instr = item;
				break;
			}
		}

		return instr;
	}
}
