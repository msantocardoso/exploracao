package br.com.cespec.exploracao.infra.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExploracaoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected List<MensagemErro> mensagens = new ArrayList<>(0);

	public ExploracaoRuntimeException() {
		super();
	}

	public ExploracaoRuntimeException(List<String> mensagens) {
		if(mensagens != null && !mensagens.isEmpty()) {
			mensagens.forEach(mensagem -> this.mensagens.add(new MensagemErro(mensagem)));
		}
	}

	public ExploracaoRuntimeException(String mensagem) {
		super(mensagem);
		addMensagem(mensagem);
	}

	public ExploracaoRuntimeException(Throwable pException) {
		super(pException);
	}

	public ExploracaoRuntimeException(String mensagem, Throwable exception) {
		super(mensagem, exception);
		addMensagem(mensagem);
	}

	private void addMensagem(String mensagem) {
		this.mensagens.add(new MensagemErro(mensagem));
	}

	public List<MensagemErro> getErros() {
		return Collections.unmodifiableList(mensagens);
	}
}
