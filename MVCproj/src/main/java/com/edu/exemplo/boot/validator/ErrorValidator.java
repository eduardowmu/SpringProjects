package com.edu.exemplo.boot.validator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

//essa classe irá traduzir a mensagem de erro, para o português, gerado pela
//página de erro error.html. Esta interface que a classe implementa é do Sprinf
//o método que deverá implementar possui 3 parâmetros
@Component
public class ErrorValidator implements ErrorViewResolver
{	@Override public ModelAndView resolveErrorView(HttpServletRequest request, 
		HttpStatus status, Map<String, Object> map) 
	{	//isso irá escrever no console todas as variáveis contidas após a requisição de erro
		//map.forEach((k,v) -> System.out.println(k + ":" + v + "\n"));
		
		//instancia do tipo ModelAndView, que possui como parâmetro a página error.html
		//essa barra "/" do parâmetro do construtor do model "/error" está gerando problemas de
		//versão entre o thymeleaf e o springboot. basta removê-la
		ModelAndView model = new ModelAndView("error");
		//instrução de recuperação o status referente ao erro e add nesse status a variável model
		model.addObject("status", status.value());
		
		//switch-case que verifica qual o tipo de erro (400, 500, etc...)
		switch(status.value())
		{	case 404:
				model.addObject("error", "Página não encontrada.");//nome da chave map (map.get("path)) 
				model.addObject("message", "A URL para a página " + map.get("path") + " não exisgte");
				break;
		
				
			case 500:
				model.addObject("error", "Ocorreu um erro interno no servidor."); 
				model.addObject("message", "Ocorreu um erro inesperado. Tente novamente mais tarde.");
				break;
				
			//este default será quando não soubermos o tipo de erro, e aí não temos como traduzir, apenas verificar
			default:
				model.addObject("error", map.get("error"));
				model.addObject("message", map.get("message"));
				break;
		}
		return model;
	}	
}