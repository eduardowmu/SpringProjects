package br.edu.fatec.topicos.controller;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("/entity")
public class EntityController 
{	@GetMapping("/entity")
	public @ResponseBody String entity()					//método do tipo resposta de corpo. foi confgurado que 
	{return "Entity";}										//este endpoint todos poderão acessar mesmo sem usuario e senha

	//isto indica que cada página que tiver isso, deverá ter 
	//papel de ADMIN ou qq outro que esteja dentro desses 
	//parenteses junto com ADMIN
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/admin/exemplo")
	public @ResponseBody String exemplo()					//este deverá apresentar usuário e senha
	{return "Exemplo";}
}