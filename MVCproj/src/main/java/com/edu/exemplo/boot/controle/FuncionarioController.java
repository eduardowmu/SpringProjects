package com.edu.exemplo.boot.controle;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Departamento;
import com.edu.exemplo.boot.domain.Funcionario;
import com.edu.exemplo.boot.domain.UF;
import com.edu.exemplo.boot.service.CargoService;
import com.edu.exemplo.boot.service.FuncionarioService;
import com.edu.exemplo.boot.validator.FuncionarioValidator;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController 
{	@Autowired private FuncionarioService service;
	
	//esta dependencia será necessário para popular a lista de cargos
	//no formulário, para o cadastro de funcionarios
	@Autowired private CargoService cargoService;
	
	/*O Spring validation faz com que este método seja o primeiro método dentro da classe
	 * FuncionarioContrller que será executado. Dessa forma ele ativa a validação e o Spring
	 * MVC vai até a classe funcionario validator para validar os campos antes de liberar o
	 * acesso da requisição ao método salvar / editar*/
	@InitBinder public void initBinder(WebDataBinder binder)
	{binder.addValidators(new FuncionarioValidator());}
	
	//mapeando para o método a ser usado
	@GetMapping("/cadastrar")	//direcionando para o diretório do arquivo html
	public String cadastrar(Funcionario funcionario)	{return	"funcionario/cadastro";}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attribute)
	{	if(result.hasErrors())	return "funcionario/cadastro";
		service.salvar(funcionario);
		attribute.addFlashAttribute("success", "Funcionario inserido com sucesso");
		//para que seja redirecionado diretamente para a página de cadastro
		return "redirect:/funcionarios/cadastrar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model)
	{	model.addAttribute("funcionario", this.service.buscarPorId(id));
		return "funcionario/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attribute)
	{	if(result.hasErrors()) return "funcionario/cadastro";
		this.service.editar(funcionario);
		attribute.addFlashAttribute("success", "Funcionario editado com sucesso.");
		return "redirect:/funcionarios/cadastrar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes attribute)
	{	this.service.excluir(id);
		attribute.addFlashAttribute("success", "Funcionario removido com sucesso.");
		return "redirect:/funcionarios/listar";
	}
	
	@GetMapping("/buscar/nome")//estamos usando o RequestParam, ao invés de PathVariable pois o nome não chegará na controller como
	public String getPorNome(@RequestParam("nome") String nome, ModelMap model)//um path da url que declaramos na notação GetMapping
	{	model.addAttribute("funcionarios", this.service.buscarPorNome(nome));//mas sim como um atributo com um parâmetrodo request
		return "funcionario/lista";//quando a gente enviar a requisição, teremos adicionada na URL aquele interrogação, nome = valor
	}								//do nome digitado no formulário de busca. É um tipo diferente de enviar um parâmetropara o controller
									//ao invés de enviar como um path, envia como um atributo do request
	
	@GetMapping("/buscar/cargo")
	public String getPorCargo(@RequestParam("id") Long id, ModelMap model)
	{	model.addAttribute("funcionarios", this.service.buscarPorCargo(id));
		return "funcionario/lista";
	}
	
	@GetMapping("/buscar/data")
	public String getPorDatas(@RequestParam("entrada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate entrada,
		@RequestParam("saida") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saida, ModelMap model)
	{	model.addAttribute("funcionarios", this.service.buscarPorDatas(entrada, saida));
		return "funcionario/lista";
	}
	
	//mapeando para o método listar
	@GetMapping("/listar") 	//direcionando para o diretório do arquivo html
	public String listar(ModelMap model)	
	{	model.addAttribute("funcionarios", service.listarTodos());
		return	"funcionario/lista";
	}
	
	@ModelAttribute("cargos")
	public List<Cargo> listaDepartamento()	{return this.cargoService.listarTodos();}
	
	//Este método nos retorna um array de unidades federativas
	@ModelAttribute("ufs")//o qual obtemos através do próprio enum, que nos
	public UF[] getUFs()	{return UF.values();}//oferece um métodos values, que nos retornará todas as constantes
												 //que temos no enum em forma de um array. Quando declaramos um enum,
												 //estes valores são chamados de ctes (classe UF).
}