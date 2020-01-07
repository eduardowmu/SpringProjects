package com.edu.exemplo.boot.controle;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.edu.exemplo.boot.domain.Departamento;
import com.edu.exemplo.boot.service.DepartamentoService;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController 
{	@Autowired//instanciado automaticamente
	private DepartamentoService service;
	
	//mapeando para o método a ser usado
	@GetMapping("/cadastrar")//direcionando para o diretório do arquivo html
	public String cadastrar(Departamento departamento)	{return	"/departamento/cadastro";}
	
	//mapeando para o método listar
	@GetMapping("/listar")//direcionando para o diretório do arquivo html
	public String listar(ModelMap model)	
	{	model.addAttribute("departamentos", service.listarTodos());
		return	"/departamento/lista";
	}
	
	//criar um método que vai receber os submits dos formulários
	//e assim a gente pega aquele objeto departamento que está sendo
	//enviado pelo formulário e agente insere no BD.
	@PostMapping("/salvar")
	public String salvar(@Valid Departamento departamento, BindingResult result, RedirectAttributes attribute)
	{	if(result.hasErrors())	return "/departamento/cadastro";
		service.salvar(departamento);
		attribute.addFlashAttribute("success", "Departamento inserido com sucesso");
		//para que seja redirecionado diretamente para a página de cadastro
		return "redirect:/departamentos/cadastrar";
	}
	
	//irá pegar pelo id do departamento que deverá ser editado
	@GetMapping("/editar/{id}")//pegando o id do departamento e o model map servirá para enviar o departamento como uma variável
	public String preEditar(@PathVariable("id") Long id, ModelMap model)//para a página de cadastro.
	{	model.addAttribute("departamento", service.buscarPorId(id));
		return "/departamento/cadastro";
	}
	
	@PostMapping("/editar")							//classe de redirecionamento de atributos
	public String editar(@Valid Departamento departamento, BindingResult result, RedirectAttributes attribute)
	{	if(result.hasErrors())	return "/departamento/cadastro";
		service.editar(departamento);
		attribute.addFlashAttribute("success", "Departamento alterado com sucesso");
		return "redirect:/departamentos/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model)
	{	//teste de se caso houver algum cargo vinculado ao depto, então
		//o mesmo não poderá ser excluído. Caso contrário, poderá ser excluído
		if(service.temCargo(id))	{model.addAttribute("fail", "Departamento não removido");}
		
		else
		{	this.service.excluir(id);
			model.addAttribute("success", "Departamento excluído com sucesso");
		}
		
		return this.listar(model);
	}
}