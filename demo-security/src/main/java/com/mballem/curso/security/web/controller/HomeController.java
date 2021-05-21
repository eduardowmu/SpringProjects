package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController 
{	//abrir pagina home ou lista de paginas
	@GetMapping({"/", "/home"})
	public String home() {return "home";}
	
	//incluindo método para abrirmos a página
	//de login.
	@GetMapping({"/login"})
	public String login() {return "login";}
	
	@GetMapping({"/login-error"})
	public String loginError(ModelMap model,
			/*Verificando se o problema é referente a um login simultâneo*/
			HttpServletRequest request) 
	{	//recuperando objeto de sessão
		HttpSession session = request.getSession();
		/*metodo que recebe como parametro o atributo da exceção lançada*/
		String lastException = String.valueOf(session.getAttribute(
				"SPRING_SECURITY_LAST_EXCEPTION"));
		if(lastException.contains(
			SessionAuthenticationException.class.getName()))
		{	model.addAttribute("alerta", "erro");
			model.addAttribute("titulo", "Acesso negado");
			model.addAttribute("texto", "Multiplos acessos com a mesma conta");
			model.addAttribute("subtexto", "Sessão expirada, realize um novo "
					+ "login.");
			return "login";
		}
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Sua sessão expirou");
		model.addAttribute("subtexto", "Acesso permitido apenas "
				+ "para cadastros já ativados");
		
		return "login";
	}
	
	@GetMapping("/expired")
	public String sessaoExpireda(ModelMap model) 
	{	model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Acesso recusado");
		model.addAttribute("texto", "Login ou senha incorretos");
		model.addAttribute("subtexto", "Você ja esta logado em outro dispositivo");
		
		return "login";
	}
	
	/*Tratamento de exceção de acesso negado*/
	@GetMapping({"/acesso-negado"})
	public String accessDenied(ModelMap model, 
			/*Com esse parâmetro conseguimos pegar o status
			 *da requisição*/
			HttpServletResponse response) 
	{	model.addAttribute("status", response.getStatus());
		model.addAttribute("error", "Acesso Negado!");
		model.addAttribute("message", "Você não tem permissão "
				+ "para acesso a esta área ou ação");
		return "error";
	}
}