package br.com.cespec.exploracao.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MensagemErro {

	private String mensagem;
}
