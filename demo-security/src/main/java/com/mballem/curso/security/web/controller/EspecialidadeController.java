package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController 
{	@Autowired private EspecialidadeService service;
	
	@GetMapping({"", "/"})
	public String abrir(Especialidade especialidade)
	{return "especialidade/especialidade";}
	
	@PostMapping("/salvar")
	public String save(Especialidade especialidade, RedirectAttributes redirect)
	{	this.service.salvar(especialidade);
		redirect.addFlashAttribute("sucesso", "Operação salva com sucesso");
		return "redirect:/especialidades";
	}
}
