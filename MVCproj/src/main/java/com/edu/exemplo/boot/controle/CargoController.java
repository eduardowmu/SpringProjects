package com.edu.exemplo.boot.controle;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Departamento;
import com.edu.exemplo.boot.service.CargoService;
import com.edu.exemplo.boot.service.DepartamentoService;
/*em todas as operações que NÃO forem do tipo redirect, devemos retirar a primeira "/"*/
@Controller
@RequestMapping("/cargos")
public class CargoController 
{	@Autowired private CargoService service;
	//devemos ter também um serviço de departamento para saber se há algum cargo 
	//dentro do departamento. Se tiver, não poderemos deletar algum departamento
	@Autowired private DepartamentoService depService;
	
	//mapeando para o método a ser usado
	@GetMapping("/cadastrar") //direcionando para o diretório do arquivo html
	public String cadastrar(Cargo cargo)	{return	"cargo/cadastro";}
	
	//mapeando para o método listar
	@GetMapping("/listar") //direcionando para o diretório do arquivo html
	public String listar(ModelMap model)	
	{	model.addAttribute("cargos", this.service.listarTodos());	
		return	"cargo/lista";
	}
	
	@PostMapping("/salvar")//a notação Valid irá dizer ao Sprting MVC que esta realizando a validação via BEAN validation para o objeto cargo, que é o objeto que vem do formulário
	public String salvar(@Valid Cargo cargo, BindingResult result, RedirectAttributes attribute)//já o BindingResult é do spring e é este que verifica se houve algum problema referentes às validações
	{	//condição que verifica se há erro, e que se for TRUE, significa que há pelo menos um campo que não passou no processo de validação e retorna para a página de origem.
		if(result.hasErrors())	return "cargo/cadastro";
		service.salvar(cargo);
		attribute.addFlashAttribute("success", "Cargo inserido com sucesso");
		//para que seja redirecionado diretamente para a página de cadastro
		return "redirect:/cargos/cadastrar";
	}
	
	@ModelAttribute("departamentos")
	public List<Departamento> listaDepartamento()	{return this.depService.listarTodos();}
	
	//irá pegar pelo id do departamento que deverá ser editado
	@GetMapping("/editar/{id}")//pegando o id do departamento e o model map servirá para enviar o departamento como uma variável
	public String preEditar(@PathVariable("id") Long id, ModelMap model)//para a página de cadastro.
	{	model.addAttribute("cargo", service.buscarPorId(id));
		return "cargo/cadastro";
	}
	
	@PostMapping("/editar")							//classe de redirecionamento de atributos
	public String editar(@Valid Cargo cargo, BindingResult result,  RedirectAttributes attribute)
	{	if(result.hasErrors())	return "cargo/cadastro";
		service.editar(cargo);
		attribute.addFlashAttribute("success", "Cargo alterado com sucesso");
		return "redirect:/cargos/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attribute)
	{	if(this.service.temFuncionario(id))	{attribute.addFlashAttribute("fail", "Cargo não removido");}
	
		else
		{	this.service.excluir(id);
			attribute.addFlashAttribute("success", "Cargo excluído com sucesso");
		}
		
		return "redirect:/cargos/listar";
	}
}