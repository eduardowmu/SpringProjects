package br.edu.fatec.topicos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//essa notação serve para mostrar p/ springboot que deve carregar essa classe
//no momento da abertura do projeto, para poder fazer a configuração de segurança.
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{	@Autowired private MyUserDetailsService detailsService;
	
	@Override//{noop} significa que a senha não é criptografada
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{	auth.userDetailsService(detailsService).passwordEncoder(new BCryptPasswordEncoder());
	
		/*auth.inMemoryAuthentication().
					withUser("usuario").
					password("{noop}admin123").
					//roles("SUPORTE").
					authorities("SUPORTE").
					and().withUser("admin").
					password("{noop}admin123").
					authorities("ADMIN");*/
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{	http.antMatcher("/**").authorizeRequests().
									antMatchers("/entity").permitAll().
									antMatchers("/admin/**").hasAnyAuthority("ADMIN").
									anyRequest().authenticated().and().formLogin();/*.permitAll().
									loginPage("/login").usernameParameter("user").
									passwordParameter("senha").defaultSuccessUrl("/admin").and().
									logout().permitAll().logoutUrl("/logout");*/
	}
}