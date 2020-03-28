package com.apirest.webflux;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
/*Essa notação significa que essa será uma classe de configuração do Springframework*/
@Configuration
public class PlaylistRouter 
{	/*Criação de um método com a notação @Bean. O método irá nos retornar um RouterFunction*/
	@Bean
	public RouterFunction<ServerResponse> route(PlaylistHandler handler)
	{	/*isso retorna um roteamento para cada um dos métodos na classe handler
	 	Que é do tipo GET, onde temos a URI*/
		return RouterFunctions.route(
				RequestPredicates.GET("/playlist").and(
				RequestPredicates.accept(org.springframework.http.MediaType.APPLICATION_JSON)), 
				//usando o método de referência da classe PlaylistHandler
				handler::findAll).andRoute(RequestPredicates.GET("/playlist/{id}")
				.and(RequestPredicates.accept(org.springframework.http.MediaType.APPLICATION_JSON)), 
				handler::findById).andRoute(RequestPredicates.POST("/playlist")
				.and(RequestPredicates.accept(org.springframework.http.MediaType.APPLICATION_JSON)), 
				handler::save);
	}
}