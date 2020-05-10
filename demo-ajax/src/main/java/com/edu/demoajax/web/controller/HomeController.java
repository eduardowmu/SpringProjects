package com.edu.demoajax.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
//calsse de controle
@Controller
public class HomeController 
{	//método que fará a abertura da página promo-add, 
	//que será uma página referenciada.
	//essa notação indica ao Spring qual o tipo de 
	//requisição, no caso GET. Temos apenas "/" pois
	@GetMapping("/") 
	public String init()//pois vamos abrir uma página a partir da instrução raiz.
	{return "redirect:/promocao/add";}
}