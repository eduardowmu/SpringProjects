package com.apirest.webflux.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apirest.webflux.document.Playlist;
import com.apirest.webflux.services.PlaylistService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class PlaylistController 
{	//criando um ponto de injeção para a nossa service
 	@Autowired private PlaylistService service;
 	
	//método que trará todas as playlists salvas no nosso BD.
 	//este método irá nos retornar um fluxo de todas as playlists salvas no BD
 	@GetMapping(value="/playlist")
	public Flux<Playlist> getPlayList()	{return this.service.findAll();}
	
 	@GetMapping(value="/playlist/{id}")
 	public Mono<Playlist> getPlayListId(@PathVariable String id)
 	{return this.service.findById(id);}
 	
 	//a notação @RequestBody é necessária para a controller saber que este método está recebendo uma
 	@PostMapping(value="/playlist")//playlist em formato de JSON
 	public Mono<Playlist> save(@RequestBody Playlist playlist) {return this.service.save(playlist);}
 	
 	//Método GET, com URI=/playlist/events, que nos retornará um stream de eventos, 
 	@GetMapping(value="/playlist/events", //e é por isso que foi definido este parâmetro "produces"
 			produces=org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
 	//o tipo Touple2 nos permite inserir dois tipos
 	public Flux<Tuple2<Long, Playlist>> getPlaylistByEvents()
 	{	//a primeira coisa é criar um flux tipo Long. Este Flux desse tipo chamado interval
 		//que é um intervalo de cada resposta ou seja de cada stream que vamos enviar ao cliente
 		//que utilizamos da própria classe do Flux do Reactor o método interval e definimos
 		//com o método Duration.ofSeconds() quantos segundos teremos entre um intervalo e
 		//outro. O intervalo foi definido como 10 segundos para poder ficar mais visível
 		Flux<Long> interval = Flux.interval(Duration.ofSeconds(10));
 		Flux<Playlist> events = service.findAll();
 		//perceba que iremos enviar duas coisas para o cliente, e para isso usamos o método zip()
 		//do Flux que pertence também a biblioteca Reactor.
		return Flux.zip(interval, events);
 	}
}
