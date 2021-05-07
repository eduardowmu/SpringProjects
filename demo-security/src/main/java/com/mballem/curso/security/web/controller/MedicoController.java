package com.mballem.curso.security.web.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UserService;

@Controller
@RequestMapping("medicos")
public class MedicoController 
{	@Autowired private MedicoService service;
	
	@Autowired private UserService userService;
	
	//abrir pagina de dados pessoais de medicos pelo MEDICO
	@GetMapping({"/dados"})
	public String abrirPorMedico(Medico medico, ModelMap model,
			/*No objeto medico não temos email do medico,
			 *portanto precisamos pegar do usuario de sessão*/
			@AuthenticationPrincipal User user)
	{	/*Se vier um ID, então estaremos enviando para salvar
	 	ou editar, caso contrario, esta sendo aberto pelo link*/
		if(medico.hasNotId())
		{	medico = this.service.findByEmail(user.getUsername());
			/*Agora devemos usar a variável model onde vamos
			 *adicionar...*/
			model.addAttribute("medico", medico);
		}
		return "medico/cadastro";
	}
	
	//salvar medico
	@PostMapping({"/salvar"})
	public String salvar(Medico medico, RedirectAttributes attr,
			/*Classe User da userDetails. Outra forma de se trabalhar
			 *com autenticação é com a classe Principal para recuperação
			 *de dados*/
			@AuthenticationPrincipal User user)
	{	/*A partir do objeto user, podemos recuperar os atributos*/
		user.getUsername();
		/*A partir da classe Principal, conseguimos obter o nome
		principal.getName();*/
		/*a partir da classe Authentication, obtemos também o nome
		auth.getName();*/
		
		/*Verifica se não existe mesmo ID de algum usuário*/
		if(medico.hasNotId() && medico.getUsuario().hasNotId())
		{	Usuario usuario = this.userService.pullByEmail(user.getUsername());
			medico.setUsuario(usuario);
		}
		this.service.salvar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		attr.addFlashAttribute("medico", medico);
		return "redirect:/medicos/dados";
	}
	
	//editar medico
	@PostMapping({"/editar"})
	public String editar(Medico medico, RedirectAttributes attr)
	{	this.service.editar(medico);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso");
		attr.addFlashAttribute("medico", medico);		
		return "redirect:/medicos/dados";
	}
	
	@GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
	public String excluirEspecialidadePorMedico(@PathVariable("idMed") Long idMed,
			@PathVariable("idEsp") Long idEsp, RedirectAttributes attr)
	{	this.service.excluirEspecialidadePorMedico(idMed, idEsp);
		attr.addFlashAttribute("sucesso", "Especialidade excluída com sucesso.");
		return "redirect:/medicos/dados";
	}
}