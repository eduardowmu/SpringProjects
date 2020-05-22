package br.edu.fatec2020.topicos.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.edu.fatec2020.topicos.domain.CarrinhoCompra;
import br.edu.fatec2020.topicos.domain.Produto;
import br.edu.fatec2020.topicos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/carrinho")
public class CarrinhoComprasController 
{	//isso é possível devido ao @Scope criado para essa classe
	private final CarrinhoCompra carrinho;
	private final ProdutoRepository repository;
	
	@GetMapping
	public ModelAndView exibirCarrinho()
	{	ModelAndView model = new ModelAndView("carrinho");
		
		model.addObject("carrinho", this.carrinho);
		
		return model;
	}
	
	@GetMapping("/add")
	public ModelAndView adicionarProduto(@RequestParam("id") Long id)
	{	ModelAndView model = new ModelAndView("carrinho");
		
		Optional<Produto> produtoOptional = this.repository.findById(id);
		//caso o produto exista
		if(produtoOptional.isPresent())
		{this.carrinho.addProduto(produtoOptional.get());}
		
		else model.addObject("erro", true);
		
		//pode haver uma situação em que não saiba o que esta acontecendo
		//na aplicação.
		model.addObject("carrinho", this.carrinho);
		
		return model;
	}
}
