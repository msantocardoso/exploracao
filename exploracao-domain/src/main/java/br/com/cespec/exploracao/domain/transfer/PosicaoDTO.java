package br.com.cespec.exploracao.domain.transfer;

import javax.validation.constraints.Min;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PosicaoDTO {

	@Min(value=0)
	private int x;

	@Min(value=0)
	private int y;

	public PosicaoDTO(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
