package com.mballem.curso.security.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailService 
{	/*Responsável pelo envio do e-mail para o servidor de email*/
	@Autowired private JavaMailSender sender;

	/*Template do Spring. Uma classe da biblioteca do Thymeleaf, responsável por comunicar com o template 
	 *da nossa página HTML importada anteriormente*/
	@Autowired private SpringTemplateEngine template;
	
	/*Método que pelos parametros, destino o receptor do email, e codigo que será enviado junto ao e-mail 
	 *que o usuário terá que clicar para que volte até a aplicação para confirmar a atualização do cadastro, 
	 *ou seja, este código será o validador dessa transação.*/
	public void enviarPedidoConfirmacaoCadastro(String destino, String codigo) throws MessagingException
	{	/*Classe que faz parte da especificação do java mail */
		MimeMessage msg = this.sender.createMimeMessage();
		
		MimeMessageHelper msgHelper = new MimeMessageHelper(msg, 
			/*Este parâmetro serve para configurar o MimeMessageHelper com o envio do topo de e-mail 
			 *que a gente está fazendo, que conterá textos HTML. imagem, e aí precisamos selecionar 
			 *este formato*/
			MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
			/*O charset por ultimo*/
			"UTF-8");
		
		/*Devemos importar o Context do pacote do thymeleaf*/
		Context context = new Context();
		context.setVariable("titulo", "Bem vindo a clinica Spring Security");
		
		/*Envio para a página a variável título para aparecer para o usuário no template do html que 
		 *estamos enviando.*/
		context.setVariable("texto", "Precisamos que confirme seu cadastro clicando aqui");
		/*Link de acesso para confirmação do cadastro*/
		context.setVariable("linkConfirmacao", "http://localhost:8080/u/confirmacao/cadastro?codigo=" + 
				codigo);
		
		/*Este método irá acessar nossa página e vai pegar as variáveis que temos em context e 
		 *substituir na página pelas instruções que temos na página.*/
		String html = template.process("email/confirmacao", context);
		
		msgHelper.setTo(destino);
		msgHelper.setText(html, true);
		msgHelper.setSubject("Confirmação de cadastro");
		msgHelper.setFrom("nao-reponder@clinica.com.br");
		/*Classe do spring para que consigamos acessar conteúdos que estão na classpath da aplicação,
		 *que é o diretório Resources*/
		msgHelper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));
		
		this.sender.send(msg);
	}
}