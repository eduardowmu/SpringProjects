package com.apirest.webflux.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apirest.webflux.document.Playlist;
import com.apirest.webflux.repository.PlaylistRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlaylistService implements IPlaylistService 
{	//ponto de injeção da PlaylistRepository, pois quando extendemos essa classe de Reactor MongoDB
	//já temos o acesso a vários métodos prontos.
	@Autowired PlaylistRepository playRepository;
	
	@Override public Flux<Playlist> findAll() {return this.playRepository.findAll();}

	@Override public Mono<Playlist> findById(String id) {return this.playRepository.findById(id);}

	@Override public Mono<Playlist> save(Playlist playlist)	{return this.playRepository.save(playlist);}
}