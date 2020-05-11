package br.edu.fatec2020.topicos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fatec2020.topicos.domain.Produto;
import br.edu.fatec2020.topicos.repository.ProdutoRepository;
import lombok.AllArgsConstructor;

@Controller
@RequestMapping("/produtos")
@AllArgsConstructor
public class ProductController 
{	private final ProdutoRepository repository;
	
	@GetMapping 
	public ModelAndView listar()
	{	ModelAndView model = new ModelAndView("produtos");
	
		model.addObject("produtos", this.repository.findAll());
		return model;
	}
	
	@PostMapping("/inserir")
	public ModelAndView salvar(Produto produto)
	{	this.repository.save(produto);
		return new ModelAndView("sucesso");
	}
}