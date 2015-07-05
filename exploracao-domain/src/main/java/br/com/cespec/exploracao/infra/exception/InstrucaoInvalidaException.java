package br.com.cespec.exploracao.infra.exception;

import java.util.List;

public class InstrucaoInvalidaException extends ExploracaoRuntimeException {

	private static final long serialVersionUID = -6434705418074169508L;

	public InstrucaoInvalidaException() {
		super();
	}

	public InstrucaoInvalidaException(String mensagem) {
		super(mensagem);
	}

	public InstrucaoInvalidaException(List<String> mensagens) {
		super(mensagens);
	}

	public InstrucaoInvalidaException(Throwable pException) {
		super(pException);
	}

	public InstrucaoInvalidaException(String mensagem, Throwable exception) {
		super(mensagem, exception);
	}
}
