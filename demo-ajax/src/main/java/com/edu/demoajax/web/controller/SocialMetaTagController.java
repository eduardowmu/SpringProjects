package com.edu.demoajax.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.demoajax.web.domain.SocialMetaTag;
import com.edu.demoajax.web.service.SocialMetaTagService;

/*Classe que irá receber a requisição AJAX para que
 as instruções das metatags sejam capturadas e depois
 devolvidas para a página*/
@Controller//transformando numa classe de controle
@RequestMapping("/meta")//o "/meta" será o path raiz de acesso a este controller.
public class SocialMetaTagController 
{	@Autowired private SocialMetaTagService service;
	
	/*metodo cujo retorno será um objeto do Spring MVC. Requisição do tipo POST, pois
	 não queremos que as informações sejam exibidas na barra de endereços. Para que
	 possamos acessar este método, teremos que ter lá no lado cliente uma URL que seja
	 "/meta/info" e que tenha um parâmetro na requisição*/
	@PostMapping("/info")
	public ResponseEntity<SocialMetaTag> getDadosViaURL(@RequestParam("url") String url)
	{	SocialMetaTag smt = this.service.getSocialMetaTagByURL(url);
		//retorno com operador ternario.
		return smt != null ? ResponseEntity.ok(smt) : ResponseEntity.notFound().build();
	}
}