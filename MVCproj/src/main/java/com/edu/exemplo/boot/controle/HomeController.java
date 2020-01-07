package com.edu.exemplo.boot.controle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController 
{	@GetMapping("/")//devemos remover a "/" do return devido ao conflito entre o Springboot e thymeleaf
	public String home()	{return "home";}
}