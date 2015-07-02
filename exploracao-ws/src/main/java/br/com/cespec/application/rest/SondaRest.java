package br.com.cespec.application.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/exploracao")
public class SondaRest {

	@RequestMapping(value="/sondas", produces="application/json", method=RequestMethod.GET)
	public String sondas() {
		return "Projeto Exploração Marte";
	}
}
