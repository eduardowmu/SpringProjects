package br.edu.fatec2020.topicos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fatec2020.topicos.domain.Produto;
import br.edu.fatec2020.topicos.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/produtos")
@AllArgsConstructor
public class ProductController 
{	private final ProdutoRepository repository;
	
	@GetMapping 			//isso serve para quando queremos editar um produto
	public ModelAndView listar(@ModelAttribute("produtoEditavel") Produto produto)
	{	ModelAndView model = new ModelAndView("produtos");
	
		model.addObject("produtos", this.repository.findAll());
		return model;
	}
	
	@PostMapping("/inserir")
	public ModelAndView salvar(Produto produto)
	{	this.repository.save(produto);
		return new ModelAndView("sucesso");
	}
	
	@GetMapping("/excluir")
	public ModelAndView excluir(@RequestParam("id") Long id)
	{	this.repository.deleteById(id);
		return new ModelAndView("sucesso");
	}
	
	@GetMapping("preAlteracao")		//isso serve para quando queremos editar um produto
	public ModelAndView preAlteracao(@RequestParam("id") Long id)
	{	Optional<Produto> optional = this.repository.findById(id);//tratamento para caso possa retornar um valor nulo
		
		if(optional.isPresent())	
		{	ModelAndView model = new ModelAndView("produtos");
			
			model.addObject("produtos", this.repository.findAll());
			//o método get() só funciona caso optional for encotrado
			model.addObject("produtoEditavel", optional.get());
			return model;
		}
		
		else return new ModelAndView("fracasso");
	}
	
	//finalmente o metodo alterar
	@PostMapping("/alterar")
	public ModelAndView alterar(Produto produto)
	{	this.repository.save(produto); //no JPA geralmente o metodo "save" faz as dias coisas
		
		return new ModelAndView("sucesso");
	}
}