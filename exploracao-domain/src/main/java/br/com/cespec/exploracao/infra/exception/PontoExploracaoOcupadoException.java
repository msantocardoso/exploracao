package br.com.cespec.exploracao.infra.exception;

import java.util.List;

public class PontoExploracaoOcupadoException extends ExploracaoRuntimeException {

	private static final long serialVersionUID = -6434705418074169508L;

	public PontoExploracaoOcupadoException() {
		super();
	}

	public PontoExploracaoOcupadoException(String mensagem) {
		super(mensagem);
	}

	public PontoExploracaoOcupadoException(List<String> mensagens) {
		super(mensagens);
	}

	public PontoExploracaoOcupadoException(Throwable pException) {
		super(pException);
	}

	public PontoExploracaoOcupadoException(String mensagem, Throwable exception) {
		super(mensagem, exception);
	}
}
