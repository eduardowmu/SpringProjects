package br.com.edu.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

import br.com.edu.domain.Client;

public interface IClientRepository extends MongoRepository<Client, String> {
	
}