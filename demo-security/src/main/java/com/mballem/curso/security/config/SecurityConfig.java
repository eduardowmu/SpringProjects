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
			.anyRequest().authenticated()
			//concatenação
			.and()
				//formulário de login
				.formLogin()
				.loginPage("/login")
				//metodo que define para onde a aplicação deve te 
				//direcionar quando tivermos sucesso na operação
				//de login. NO caso, direcionando para qq outra
				//página da nossa aplicação.
				.defaultSuccessUrl("/", true)
				//mas se falhar a autenticação
				.failureUrl("/login-error")
				//obriga que todos os usuarios tenha permissão para
				//acessar a página de login e a de erro
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/");
			
		//super.configure(http);
	}	
}