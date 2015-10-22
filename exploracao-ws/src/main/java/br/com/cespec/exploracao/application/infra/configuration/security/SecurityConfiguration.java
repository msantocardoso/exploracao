package br.com.cespec.exploracao.application.infra.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests().anyRequest().hasAnyRole("ADMIN","USER")
			.and().httpBasic();

		http.csrf().disable();
	}

	@Bean
	public DigestAuthenticationEntryPoint digestEntryPoint() {
		DigestAuthenticationEntryPoint digestAuthenticationEntryPoint = new DigestAuthenticationEntryPoint();
		digestAuthenticationEntryPoint.setKey("key");
		digestAuthenticationEntryPoint.setNonceValiditySeconds(120);
		digestAuthenticationEntryPoint.setRealmName("Serviço Exploração espacial");
		return digestAuthenticationEntryPoint;
	}

	public DigestAuthenticationFilter digestAuthenticationFilter(
			DigestAuthenticationEntryPoint digestAuthenticationEntryPoint)
			throws Exception {
		
		DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();

		digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());
		digestAuthenticationFilter.setUserDetailsService(userDetailsServiceBean());

		return digestAuthenticationFilter;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("123").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("msantocardoso").password("123").roles("USER");
	}
}
