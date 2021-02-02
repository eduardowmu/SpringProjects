package com.mballem.curso.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends 
	WebSecurityConfigurerAdapter
{	@Override protected void configure(HttpSecurity http) 
		throws Exception
	{	/*Essa é a configuração mais básica que temos para
	 	trabalhar com Spring Security*/
		http.authorizeRequests()
			//liberando webjars (estaticos). os duplos
			//asteristicos, estamos liberando tudo que estiver
			//dentro do diretório, no caso de um exemplo abaixo, 
			//webjars.
			.antMatchers("/webjars/**", "/css/**", "/image/**", 
				"/js/**").permitAll()
			//liberando a pagina HOME
			.antMatchers("/", "/home")
			//significa que tal URI não irá precisar de
			//autenticação
			.permitAll()
			.anyRequest().authenticated();
		//super.configure(http);
	}	
}