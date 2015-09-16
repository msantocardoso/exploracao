package br.com.cespec.exploracao.application.rest;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.cespec.exploracao.domain.model.Direcao;
import br.com.cespec.exploracao.domain.model.Planalto;
import br.com.cespec.exploracao.domain.model.Sonda;
import br.com.cespec.exploracao.domain.repository.Sondas;
import br.com.cespec.exploracao.domain.transfer.InstrucaoDTO;
import br.com.cespec.exploracao.domain.transfer.PosicaoDTO;
import br.com.cespec.exploracao.domain.transfer.SondaDTO;
import br.com.cespec.exploracao.infra.exception.ExploracaoRuntimeException;

@RestController
@RequestMapping(value="/exploracao")
public class SondaRest {

	@Autowired
	Planalto planalto;

	@Autowired
	Sondas sondaRepository;

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/exibir/area", produces="text/plain", method=RequestMethod.GET)
	public String exibirAreaExploracao() {
		String area = "";
		try {
			area = planalto.getAreaExploracao();
		} catch (ExploracaoRuntimeException e) {
			area = e.getMessage();
		}

		return area;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value="/iniciar/area", produces="application/json", method=RequestMethod.POST)
	public void iniciarAreaExploracao(@Valid @RequestBody PosicaoDTO posicao) {
		planalto.inicializar(posicao.getX(), posicao.getY());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/sondas", produces="application/json", method=RequestMethod.GET)
	public List<SondaDTO> consultarSondas() {

		List<Sonda> sondas = sondaRepository.buscarTodas();

		List<SondaDTO> listSondas = new ArrayList<>(sondas.size());

		sondas.forEach(snd -> {
			listSondas.add(new SondaDTO(snd));
		});

		return listSondas;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/sondas/{id}", produces="application/json", method=RequestMethod.GET)
	public SondaDTO consultarSonda(@PathVariable("id") @NotNull Long id) {

		Sonda sonda = sondaRepository.buscar(id);

		SondaDTO sondaTransfer = null;

		if(sonda!=null) {
			sondaTransfer= new SondaDTO(sonda);
		}

		return sondaTransfer;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(value="/sondas", produces="application/json", method=RequestMethod.POST)
	public void adicionarSonda(@Valid @RequestBody SondaDTO sonda) {

		PosicaoDTO posicao = sonda.getPosicao();
		Direcao direcao = Direcao.getDirecao(sonda.getDirecao());

		planalto.adicionarSonda(posicao.getX(), posicao.getY(), direcao);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/sondas/{id}/executar/instrucoes/{instrucoes}", produces="application/json", method=RequestMethod.PUT)
	public SondaDTO executarInstrucoes(@PathVariable("id") @NotNull Long id, @PathVariable("instrucoes") @NotEmpty String instrucoes) {

		Sonda sonda = planalto.executarInstrucoes(id, instrucoes);

		SondaDTO sondaDTO = new SondaDTO(sonda);

		return sondaDTO;
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/sondas/executar/instrucoes", produces="application/json", method=RequestMethod.PUT)
	public List<SondaDTO> executarListaInstrucoes(@Valid @RequestBody List<InstrucaoDTO> instrucoes) {

		List<Sonda> sondas = planalto.executarInstrucoes(instrucoes);

		return SondaDTO.toSondas(sondas);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value="/sondas/{id}", produces="application/json", method=RequestMethod.DELETE)
	public void removerSonda(@PathVariable("id") @NotNull Long id) {
		planalto.removerSonda(id);
	}

}
