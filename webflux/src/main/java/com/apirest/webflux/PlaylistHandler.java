package com.apirest.webflux;

import java.awt.PageAttributes.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.apirest.webflux.document.Playlist;
import com.apirest.webflux.services.PlaylistService;
import reactor.core.publisher.Mono;
/*essa notação servirá para mostrar que é um Bean que vai ser gerenciado pelo
Spring framework
*/
@Component
public class PlaylistHandler 
{	/*criar um pto de injeção na playlist service para que possamos então usar
 	os métodos de transação com o banco*/
	@Autowired PlaylistService service;
	
	/*Método findAll(), que vai nos retornar todas as playlists do BD.
	O tipo de retorno será do tipo "Mono" pois o parâmetro ServerReponse faz parte da
	API reativa do Spring Framework e vamos então montar uma resposta para enviar como
	retorno. Então vai ser um único elemento e vai pegar toda a lista, montar uma resposta
	e retornar. Então temos que determina-la como tipo mono*/
	public Mono<ServerResponse> findAll(ServerRequest request)
	{	/*método ok() da biblioteca reativa do Spring framework e vamos então determinar
	 	o content type dessa resposta e o body. Então definimos:*/
	 	return ServerResponse.ok()
	 			//1 - Content Type, que será um objeto no formato JSON
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				/*2 - Body, onde determinamos o método e o tipo de retorno que teremos*/
				.body(this.service.findAll(), Playlist.class);
	}
	
	//mesmo método porém com filtro pelo ID
	public Mono<ServerResponse> findById(ServerRequest request)
	{	/*antes de obter e resposta, precisa receber um pathVavriable, que no caso é o nosso
	 	ID, pois no caso a gente quer encontrar a unica playlist.*/
		String id = request.pathVariable("id");
	 	return ServerResponse.ok()
	 			//1 - Content Type, que será um objeto no formato JSON
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				/*2 - Body, onde determinamos o método e o tipo de retorno que teremos*/
				.body(this.service.findById(id), Playlist.class);
	}
	
	public Mono<ServerResponse> save(ServerRequest request)
	{	/*iremos ver qual a playlist que esta vindo através de uma requisição via POST
	 	para que possamos passar para o método save() na nossa service e salvar no BD.*/
		final Mono<Playlist> playlist = request.bodyToMono(Playlist.class);
		return ServerResponse.ok()
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromPublisher(playlist.flatMap(service::save), Playlist.class));
	}
}
