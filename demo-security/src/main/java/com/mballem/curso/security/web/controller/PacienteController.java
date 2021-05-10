package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.PacienteService;
import com.mballem.curso.security.service.UserService;

@Controller
@RequestMapping("pacientes")
public class PacienteController 
{	@Autowired private PacienteService service;
	
	@Autowired private UserService userService;
	
	@GetMapping("/dados")
	public String cadastrar(Paciente paciente, ModelMap model, 
			@AuthenticationPrincipal User user)
	{	paciente = this.service.buscarPorPacienteByEmail(user.getUsername());
		/*Verificando se o usuário existe pelo ID*/
		if(paciente.hasNotId())
		{paciente.setUsuario(new Usuario(user.getUsername()));}
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, ModelMap model, 
			@AuthenticationPrincipal User user)
	{	Usuario usuario = this.userService.pullByEmail(user.getUsername());
		if(UserService.isSenhaCorreta(paciente.getUsuario().getSenha(), 
				usuario.getSenha()))
		{	paciente.setUsuario(usuario);
			this.service.salvar(paciente);
			model.addAttribute("sucesso", "Dados salvos com sucesso.");
		}
		
		else
		{	model.addAttribute("falha", "Sua senha não confere, "
				+ "tente novamente.");
		}
		return "paciente/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user)
	{	Usuario usuario = this.userService.pullByEmail(user.getUsername());
		if(UserService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha()))
		{	this.service.editar(paciente);
			model.addAttribute("sucesso", "Dados editados com sucesso.");
		}
		
		else {model.addAttribute("falha", "Sua senha não confere, tente novamente.");}
		
		return "paciente/cadastro";
	}
}