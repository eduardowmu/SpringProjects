package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletResponse;

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
	public String loginError(ModelMap model) 
	{	model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos");
		model.addAttribute("subtexto", "Acesso permitido apenas "
				+ "para cadastros já ativados");
		
		return "home";
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