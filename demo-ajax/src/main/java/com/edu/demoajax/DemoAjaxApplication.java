package com.edu.demoajax;

import org.directwebremoting.spring.DwrSpringServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

import com.edu.demoajax.web.domain.SocialMetaTag;
import com.edu.demoajax.web.service.SocialMetaTagService;
/*essa classe é responsável pela inicialização do SPRING BOOT
 * pelo método MAIN. Como essa classe tem a notação SpringBootApplication
 * Esta passa a ser a classe principal do projeto, que vai inicializar
 * o SPRING BOOT*/
/*A anotação ImportResource */
@ImportResource(locations = "classpath:dwr-spring.xml")
@SpringBootApplication
public class DemoAjaxApplication implements CommandLineRunner
{	public static void main(String[] args) 
	{SpringApplication.run(DemoAjaxApplication.class, args);}
	
	@Autowired SocialMetaTagService service;
	
	@Override public void run(String... args) throws Exception 
	{	/*
		SocialMetaTag meta = service.getSocialMetaTagByURL("https://www.udemy.com/course/the-complete-wordpress-website-business-course/");
		//iremos imprimir as informações no console retornadas pelo objeto org
		System.out.println(meta.toString());*/
		
		/*
		SocialMetaTag twitter = service.getTwitterCardByURL("https://www.udemy.com/course/the-complete-wordpress-website-business-course/");
		//iremos imprimir as informações no console retornadas pelo objeto org
		System.out.println(twitter.toString());
		*/
	} 
	
	/*método público do tipo ServletRegistrationBean. A anotação ira transformar nosso
	método em um Bean gerenciado pelo spring*/
	@Bean
	public ServletRegistrationBean<DwrSpringServlet> dwrSpringServlet()
	{	DwrSpringServlet dwrServlet = new DwrSpringServlet();
	
		ServletRegistrationBean<DwrSpringServlet> registrationBean = 
			new ServletRegistrationBean<>(dwrServlet, "/dwr/*");
		
		//incluindo o objeto para inserir parametros de inicialização
		//e este recuro serve para habilitar o recusro de debug
		registrationBean.addInitParameter("debug", "true");
		
		//permite o uso do AJAX reverso
		registrationBean.addInitParameter("activeReverseAjaxEnabled", "true");
		
		return registrationBean;
	}
}