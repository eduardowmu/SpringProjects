package com.mballem.curso.security.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String save(Especialidade especialidade, 
			RedirectAttributes redirect)
	{	this.service.salvar(especialidade);
		redirect.addFlashAttribute("sucesso", 
				"Operação salva com sucesso");
		return "redirect:/especialidades";
	}
	
	/*Map que pegamos na requisição ajax especialidade.js*/
	@GetMapping({"/datatables/server"})
	public ResponseEntity<?> getEspecialidade(HttpServletRequest request)
	{	return ResponseEntity.ok(
			this.service.buscarEspecialidades(request));
	}
	
	/*O primeiro método será aquele que irá receber a
	 *requisição para edição, através do ID, envia os dados
	 *para os campos de edição do formulário.*/
	@GetMapping({"/editar/{id}"})/*path variable para capturar o ID*/
	public String preEditar(@PathVariable("id") Long id, ModelMap model)
	{	model.addAttribute("especialidade", this.service.buscarPorId(id));
		return "especialidade/especialidade";
	}
	
	@GetMapping({"/excluir/{id}"})/*path variable para capturar o ID*/
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attr)
	{	this.service.removerPorId(id);
		attr.addFlashAttribute("Sucesso", "Operação realizada com sucesso");
		return "redirect:/especialidades";
	}
	
	@GetMapping({"/titulo"})
	public ResponseEntity<?> getEspecialidadePorTermo(@RequestParam("termo") String termo)
	{	List<String> especialidades = this.service.buscarEspecialidadesPorTermo(termo);
		return ResponseEntity.ok(especialidades);
	}
	
	@GetMapping({"/datatables/server/medico/{id}"})
	public ResponseEntity<?> getEspecialidadePorMedico(@PathVariable("id") Long id,
			HttpServletRequest request)
	{return ResponseEntity.ok(this.service.buscarEspecialidadesPorMedico(id, request));}
}
