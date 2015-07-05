package br.com.cespec.exploracao.domain.transfer;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class InstrucaoDTO {

	@NotNull
	private Long sondaId;

	@NotEmpty
	private String instrucoes;
}
