package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UserService;

@EnableWebSecurity
public class SecurityConfig extends 
	WebSecurityConfigurerAdapter
{	//criado com a intensão de evitar de escrever STRINGS direto
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

	//injeção de dependencias
	@Autowired private UserService usuarioService;
	
	@Override protected void configure(HttpSecurity http) 
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
			//adicionar acessos privados para o MEDICO
			.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
			//adicionar acessos privados para o ADMIN
			.antMatchers("/u/**").hasAuthority(ADMIN)
			//acessos privados medicos.
			/*Agora precisaremos dizer que iremos liberar esses acessos
			 *para o médico e para o ADMIN. Com essa configuração, um ADMIN
			 *só não será capaz de deletar um médico*/
			.antMatchers("/medicos/dados", "/medicos/salvar", 
					"/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
			/*Quando add /medicos /dados para o ADMIN, estamos desbloqueando 
			 *o acesso para que o ADMIN tenha então acesso a essa URI, como 
			 *demos acesso a essa URI para o ADMIN, tudo que vier para baixo 
			 *vai estar com a URI bloqueada, mesmo com este processo feito aqui.*/
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			//acesso privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
			//acesso privados especialidades
			.antMatchers("/datatables/server/medico/{id}").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)
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
				.logoutSuccessUrl("/")
			.and()
			/*Capturando a exceção quando tivermos um acesso negado
			 *Estamos então dizendo para o Spring Security que queremos
			 *capturar a exceção, que deve ser a exceção do tipo access
			 *denied exception. Então automaticamente o Spring vai capturar
			 *esse tipo de exceção e vai enviar um redirecionamento lá para
			 *o método que vamos criar na controller.*/
			.exceptionHandling().accessDeniedPage("/acesso-negado");
		//super.configure(http);
	}

	/*Método que será sobreescrito de WebSecurityConfigureAdapter*/
	@Override protected void configure(
			AuthenticationManagerBuilder auth) throws Exception 
	{	/*Este método espera que passemos como parâmetro um 
		objeto do tipo userDetailsService, que é nosso service,
		pois esta implementa um UserDetailService.*/
		auth.userDetailsService(this.usuarioService)
			/*O objetivo desse método é informar qual o tipo de
			 *criptografia que será utilizado. Quando formos fazer
			 *Um cadastro de usuário iremos pegar a senha digitado
			 *no cadastro e vai criptografar e salvar a senha
			 *criptografada. Quando o spring receber os dados da
			 *consulta que foi passado para ele e for comparar com
			 *a senha digitada, será necessário criptografar a senha
			 *para ver se essa criptografia é igual aquela que
			 *temos no BD*/
			.passwordEncoder(new BCryptPasswordEncoder());
	}	
}