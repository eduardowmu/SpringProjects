package com.mballem.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// abrir pagina home ou lista de paginas
	@GetMapping({"/", "/home"})
	public String home() {return "home";}	
}
