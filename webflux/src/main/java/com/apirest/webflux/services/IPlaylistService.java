package com.apirest.webflux.services;

import com.apirest.webflux.document.Playlist;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IPlaylistService 
{	//quando estamos trabalhando com programação síncrona normal, quando utilizavamos
	//o método findAll(), retornava pra gente uma lista, de um determinado objeto neste
	//caso a playlist. Mas quandoe estamos trabalhando com programação reativa assincrona
	//(webflux), este nao retornará uma Playlist mas sim uma lista (tipo flux) que vários
	//elementos.
	Flux<Playlist> findAll();
	//o "Mono" garante que retornará apenas uma ocorrência
	Mono<Playlist> findById(String id);
	Mono<Playlist> save(Playlist playlist);
}
