package br.com.erudio.restwithspringbootudemy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.erudio.restwithspringbootudemy.model.Person;
/*Só com essa estrutura já nos permite fazer algumas 
 *operações como salvar, delete e findAll*/
@Repository
public interface PersonRepository extends 
	JpaRepository<Person, Long> {}
