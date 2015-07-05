package br.com.cespec.exploracao.infra.exception;

import java.util.List;

public class PosicaoExploracaoInvalidoException extends ExploracaoRuntimeException {

	private static final long serialVersionUID = -6434705418074169508L;

	public PosicaoExploracaoInvalidoException() {
		super();
	}

	public PosicaoExploracaoInvalidoException(String mensagem) {
		super(mensagem);
	}

	public PosicaoExploracaoInvalidoException(List<String> mensagens) {
		super(mensagens);
	}

	public PosicaoExploracaoInvalidoException(Throwable pException) {
		super(pException);
	}

	public PosicaoExploracaoInvalidoException(String mensagem, Throwable exception) {
		super(mensagem, exception);
	}
}
