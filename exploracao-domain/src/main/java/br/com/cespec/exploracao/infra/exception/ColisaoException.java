package br.com.cespec.exploracao.infra.exception;

import java.util.List;

public class ColisaoException extends ExploracaoRuntimeException {

	private static final long serialVersionUID = -6434705418074169508L;

	public ColisaoException() {
		super();
	}

	public ColisaoException(String mensagem) {
		super(mensagem);
	}

	public ColisaoException(List<String> mensagens) {
		super(mensagens);
	}

	public ColisaoException(Throwable pException) {
		super(pException);
	}

	public ColisaoException(String mensagem, Throwable exception) {
		super(mensagem, exception);
	}
}
