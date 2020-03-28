package com.apirest.webflux.controller;

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

//@RestController
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
}
