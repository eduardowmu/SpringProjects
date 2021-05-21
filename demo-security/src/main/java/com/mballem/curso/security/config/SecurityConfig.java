package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UserService;
/*Estamos habilitando o uso de anotaÃ§Ãµes para partes de
 *seguranÃ§a.*/
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{	//criado com a intensÃ£o de evitar de escrever STRINGS direto
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

	//injeÃ§Ã£o de dependencias
	@Autowired private UserService usuarioService;
	
	@Override protected void configure(HttpSecurity http) 
		throws Exception
	{	/*Essa Ã© a configuraÃ§Ã£o mais bÃ¡sica que temos para
	 	trabalhar com Spring Security*/
		http.authorizeRequests()
			//liberando webjars (estaticos). os duplos asteristicos, estamos liberando tudo 
			//que estiver dentro do diretÃ³rio, no caso de um exemplo abaixo, webjars.
			.antMatchers("/webjars/**", "/css/**", "/image/**", 
				"/js/**").permitAll()
			//liberando a pagina HOME
			.antMatchers("/", "/home", "/expired")
			//significa que tal URI nÃ£o irÃ¡ precisar de
			//autenticaÃ§Ã£o
			.permitAll()
			/*adicionando novas URIs que serÃ£o publicas do cadastro de pacientes*/
			.antMatchers("/u/novo/cadastro", "/u/cadastro/realizado", 
					"/u/cadastro/paciente/salvar").permitAll()
			.antMatchers("/u/confirmacao/cadastro").permitAll()
			/*liberação para recuperação de senha, para que possamos acessar a pagina que será
			 *usada para recuperação de senha.*/
			.antMatchers("/u/p/**").permitAll()
			//adicionar acessos privados para o MEDICO
			.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
			//adicionar acessos privados para o ADMIN
			.antMatchers("/u/**").hasAuthority(ADMIN)
			/*Liberando para o paciente a lista de mÃ©dicos*/
			.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(MEDICO, PACIENTE)
			//acessos privados medicos.
			/*Agora precisaremos dizer que iremos liberar esses acessos
			 *para o mÃ©dico e para o ADMIN. Com essa configuraÃ§Ã£o, um ADMIN
			 *sÃ³ nÃ£o serÃ¡ capaz de deletar um mÃ©dico*/
			.antMatchers("/medicos/dados", "/medicos/salvar", 
					"/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
			/*Quando add /medicos /dados para o ADMIN, estamos desbloqueando 
			 *o acesso para que o ADMIN tenha entÃ£o acesso a essa URI, como 
			 *demos acesso a essa URI para o ADMIN, tudo que vier para baixo 
			 *vai estar com a URI bloqueada, mesmo com este processo feito aqui.*/
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			//acesso privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
			//acesso privados especialidades
			.antMatchers("/datatables/server/medico/{id}").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)
			.anyRequest().authenticated()
			//concatenaÃ§Ã£o
			.and()
				//formulÃ¡rio de login
				.formLogin()
				.loginPage("/login")
				//metodo que define para onde a aplicaÃ§Ã£o deve te 
				//direcionar quando tivermos sucesso na operaÃ§Ã£o
				//de login. NO caso, direcionando para qq outra
				//pÃ¡gina da nossa aplicaÃ§Ã£o.
				.defaultSuccessUrl("/", true)
				/*mas se falhar a autenticaÃ§Ã£o. Este mÃ©todo que o spring
				 *executa quando o usuÃ¡rio nÃ£o for confirmado na tentativa
				 *de autenticaÃ§Ã£o, mesmo tendo colocado essa instruÃ§Ã£o.*/
				.failureUrl("/login-error")
				//obriga que todos os usuarios tenha permissÃ£o para
				//acessar a pÃ¡gina de login e a de erro
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
			/*Capturando a exceÃ§Ã£o quando tivermos um acesso negado
			 *Estamos entÃ£o dizendo para o Spring Security que queremos
			 *capturar a exceÃ§Ã£o, que deve ser a exceÃ§Ã£o do tipo access
			 *denied exception. EntÃ£o automaticamente o Spring vai capturar
			 *esse tipo de exceÃ§Ã£o e vai enviar um redirecionamento lÃ¡ para
			 *o mÃ©todo que vamos criar na controller.*/
			.exceptionHandling().accessDeniedPage("/acesso-negado")
			/*Configuração do remember-me, com validade de aproximadamente de 2 semanas.*/
			.and().rememberMe();
		//super.configure(http);
		
		/*Método que vai dizer para a configuração que é para o Spring olhar para a parte de gerenciamento
		 *de sessão.*/
		http.sessionManagement()
			/*Indicando para a configuração quantos dispositivos serão capazes de manter a sessão conectados
			 *simultaneamente*/
			.maximumSessions(1)
			/*Passar um parâmetro TRUE para que informe a configuração que quando estivermos logado, em um
			 *dispositivo, não poderemos logar com outro*/
			//.maxSessionsPreventsLogin(true)
			/*indicando, para o primeiro acesso que não tem mais ação à primeira aplicação*/
			.expiredUrl("/expired")
			/*Permitindo a realização de login mesmo com uma sessão já ativa em um outro navegador*/
			.maxSessionsPreventsLogin(false)
			/*Método onde passamos um parametro do tipo SessionRegistry, que será o BEAN que criamos 
			 *abaixo*/
			.sessionRegistry(this.sessionRegistry());
		
		http.sessionManagement()
			/*Esse método nos dará acesso à próxima operação que iremos usar.*/
			.sessionFixation()
			/*Esta cria uma nova sessão e valida a sessão que ja existia, porém como irá saber que tal
			 *sessão já existia?*/
			.newSession()
			/*a nova sessão irá saber através dessa nova operação*/
			.sessionAuthenticationStrategy(this.sessionAuthStrategy());
	}

	/*MÃ©todo que serÃ¡ sobreescrito de WebSecurityConfigureAdapter*/
	@Override protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{	/*Este mÃ©todo espera que passemos como parÃ¢metro um objeto do tipo userDetailsService, que Ã© nosso 
		service, pois esta implementa um UserDetailService.*/
		auth.userDetailsService(this.usuarioService)
		/*O objetivo desse mÃ©todo Ã© informar qual o tipo de criptografia que serÃ¡ utilizado. Quando formos fazer
		 *Um cadastro de usuÃ¡rio iremos pegar a senha digitado no cadastro e vai criptografar e salvar a senha
		 *criptografada. Quando o spring receber os dados da consulta que foi passado para ele e for comparar com
		 *a senha digitada, serÃ¡ necessÃ¡rio criptografar a senha para ver se essa criptografia Ã© igual aquela que 
		 *temos no BD*/
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/*Novo método que ficará responsável pela restrição de sessões simultâneas. A interface SessionRegistry,
	 *do pacote org.springframework.security.core.session. Esse BEAN será responsável por passar para uma
	 *outra configuração que faremos mais tarde a sessão que o spring possui referente a cada login que está
	 *sendo feito na aplicação. Só que além disso precisamos criar uma espécie de filtro a partir de um listener
	 *e portanto iremos criar um novo método, que também será um BEAN.*/
	@Bean public SessionRegistry sessionRegistry()
	{return new SessionRegistryImpl();}
	
	/*Com esse método estamos registrando um servlet na nossa aplicação e este que ficará responsável por cuidar
	 *de todas as operações de login que estão sendo realizadas.*/
	@Bean public ServletListenerRegistrationBean<?> servletListenerRegistrationBean()
	{return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());}
	
	/*Um BEAN indica que este método é gerenciado direto pelo Spring*/
	@Bean public SessionAuthenticationStrategy sessionAuthStrategy()
	{	/*retorna uma instancia que implementa essa interface, recebendo um objeto do tipo SessionRegistry*/
		return new RegisterSessionAuthenticationStrategy(this.sessionRegistry());
	}
}