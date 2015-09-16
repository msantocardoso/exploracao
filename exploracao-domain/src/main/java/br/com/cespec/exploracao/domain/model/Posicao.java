package br.com.cespec.exploracao.domain.model;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Posicao {

	@Min(value=0)
	@Getter
	private int x;

	@Min(value=0)
	@Getter
	private int y;
}
