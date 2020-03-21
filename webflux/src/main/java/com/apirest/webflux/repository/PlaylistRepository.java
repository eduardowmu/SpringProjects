package com.apirest.webflux.repository;
import com.apirest.webflux.document.*;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
/*Apenas com essa interface ja teremos acesso a vários métodos prontos como o findAll()
 * E muitos outros*/
public interface PlaylistRepository extends ReactiveMongoRepository<Playlist, String>{}