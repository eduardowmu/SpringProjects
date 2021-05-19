package com.mballem.curso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mballem.curso.security.service.EmailService;
/*Implementando metodo para testar o envio de e-mail*/
@SpringBootApplication
public class DemoSecurityApplication// implements CommandLineRunner
{	public static void main(String[] args) 
	{	//System.out.println(new BCryptPasswordEncoder().encode("SENHA"));
		SpringApplication.run(DemoSecurityApplication.class, args);
	}
//
//	@Autowired private EmailService service;
//	
//	@Override public void run(String... args) throws Exception
//	{this.service.enviarPedidoConfirmacaoCadastro("ewmurakoshi@gmail.com", "1234");}

	/*Injetando o JavaMailSender*/
	//@Autowired private JavaMailSender sender;
	
	/*inserindo nosso teste*/
//	@Override public void run(String... args) throws Exception 
//	{	SimpleMailMessage mailMsg = new SimpleMailMessage();
//		//Seta para quem iremos enviar o e-mail
//		mailMsg.setTo("eduwmura@gmail.com");
//		mailMsg.setText("Teste, favor ignorar");
//		mailMsg.setSubject("Teste");
//		sender.send(mailMsg);
//	}
}