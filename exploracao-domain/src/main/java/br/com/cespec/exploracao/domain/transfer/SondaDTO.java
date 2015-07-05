package br.com.cespec.exploracao.domain.transfer;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Posicao;
import br.com.cespec.exploracao.domain.model.Sonda;

@Data
@NoArgsConstructor
public class SondaDTO {

	private Long id;

	@NotNull
	PosicaoDTO posicao;

	@NotNull
	private String direcao;

	public SondaDTO(int x, int y, String direcao) {
		this.posicao = new PosicaoDTO(x, y);

		this.direcao = direcao;
	}

	public SondaDTO(Sonda sonda) {
		this(sonda.getPosicao().getX(), sonda.getPosicao().getY(), sonda.getDirecao().name());

		this.id = sonda.getId();
	}

	public Sonda toSonda() {

		return new Sonda(posicao.getX(), posicao.getY(), Direcao.getDirecao(direcao));
	}

	public static List<SondaDTO> toSondas(List<Sonda> sondas) {
		List<SondaDTO> sondasTransf = new ArrayList<>(0);

		sondas.forEach(snd -> {
			Posicao posicao = snd.getPosicao();

			SondaDTO sondaDTO = new SondaDTO(posicao.getX(), posicao.getY(), snd.getDirecao().name());
			sondaDTO.setId(snd.getId());

			sondasTransf.add(sondaDTO);
		});

		return sondasTransf;
	}
}
