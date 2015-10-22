package br.com.cespec.exploracao.application.infra.configuration;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import br.com.cespec.exploracao.application.infra.configuration.security.SecurityConfiguration;

@Configuration
@EnableWebMvc
@Import({ SecurityConfiguration.class })
@ComponentScan(basePackages = {"br.com.cespec"})
public class WebConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {

		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();

		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.enable(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS);
		objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.setDateFormat(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);

		jsonConverter.setObjectMapper(objectMapper);

		return jsonConverter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		converters.add(customJackson2HttpMessageConverter());

		super.addDefaultHttpMessageConverters(converters);
	}

	@Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
