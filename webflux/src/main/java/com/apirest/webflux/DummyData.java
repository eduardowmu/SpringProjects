package com.apirest.webflux;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.apirest.webflux.document.Playlist;
import com.apirest.webflux.repository.PlaylistRepository;
import reactor.core.publisher.Flux;
/*
@Component
public class DummyData implements CommandLineRunner
{	//Playlist como atributo
	private final PlaylistRepository playlistRepository;
	
	//constructor
	public DummyData(PlaylistRepository playlistRepository)
	{this.playlistRepository = playlistRepository;}*/
	
	/*Iremos usar o método save desse repository para salvar algumas playlist. Este método
	irá deletar toda a coleção de playlist caso ja esteja com algo iniciada com algum dado 
	populado, esta deletando tudo e através de um fluxo de Stream, irá inserir alguns nomes
	de playlist já pré determinado, e no método map() irá inserir alguns nomes juntamente com
	o ID que será gerado através do ID randomico. Ao terminar então irá salvar todos os dados
	no BD
	@Override
	public void run(String... args) throws Exception 
	{	this.playlistRepository.deleteAll().thenMany(
			Flux.just("API REST Spring Boot", "Deploy de uma java no Atlas cloud", 
			"Java 8 GitHub", "Spring Security", "Web Service RESTUFULL", "Bean no " +
			"Spring Framework").map(nome -> new Playlist(UUID.randomUUID().toString(), 
			nome)).flatMap(this.playlistRepository::save)).subscribe(System.out::println);
	}
}*/