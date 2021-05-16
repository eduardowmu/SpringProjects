package com.mballem.curso.security.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.mballem.curso.security.exception.AccessDeniedException;

/*Essa notação serve como se fosse um ouvinte na sua aplicação Iremos criar umas regras
 *dentro dessa classe e essa notação irá esperar que algumas dessas regras sejam verdadeiras e quando
 *forem, esta classe ativa o método referente a regra ativada. As regras que criaremos aqui são baseadas
 *em exceções.*/
@ControllerAdvice
public class ExceptionController 
{	/*Parâmetro do método UsernameNotFoundException é do proprio
 	Spring. Abaixo temos outro notação do spring, que deve receber como parâmetro a exception que queremos tratar.*/
	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView User404Exception(UsernameNotFoundException uex)
	{	ModelAndView mv = new ModelAndView("error");
		//para o tipo ModelAndView, usamos o objeto e não atributo
		mv.addObject("status", 404);
		mv.addObject("error", "Usuário não encontrado");
		mv.addObject("message", uex.getMessage());
		return mv;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ModelAndView userAccess403(AccessDeniedException uex)
	{	ModelAndView mv = new ModelAndView("error");
		//para o tipo ModelAndView, usamos o objeto e não atributo
		mv.addObject("status", 403);
		mv.addObject("error", "Usuário não encontrado");
		mv.addObject("message", uex.getMessage());
		return mv;
	}
}