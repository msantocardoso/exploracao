package br.com.cespec.exploracao.application.infra.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.cespec.exploracao.infra.exception.ExploracaoRuntimeException;
import br.com.cespec.exploracao.infra.exception.MensagemErro;

@ControllerAdvice
public class GlobalExceptionHandler  {

	@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    List<MensagemErro> handleException(ExploracaoRuntimeException ex) {
        return ex.getErros();
    }

	@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
	List<MensagemErro> handleException(MethodArgumentNotValidException ex) {
		List<MensagemErro> erros = new ArrayList<>();
		BindingResult bindingResult = ex.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		for (FieldError fieldError : fieldErrors) {
			String erro = fieldError.getField() +": "+ fieldError.getDefaultMessage();

			erros.add(new MensagemErro(erro));
		}

        return erros;
    }

}