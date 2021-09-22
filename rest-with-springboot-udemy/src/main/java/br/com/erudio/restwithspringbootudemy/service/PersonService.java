package br.com.erudio.restwithspringbootudemy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.restwithspringbootudemy.exception.ResourceNotFoundException;
import br.com.erudio.restwithspringbootudemy.model.Person;
import br.com.erudio.restwithspringbootudemy.repository.PersonRepository;
/*Service para simular um ID de um BD. Todos os métodos
 *novos são do spring data padrão*/
@Service
public class PersonService 
{	@Autowired PersonRepository personRepository;
	
	public Person create(Person person) 
	{return this.personRepository.save(person);}
	
	public Person update(Person person) 
	{	/*Busca pela entidade pelo ID, que não deve
	 	ser alterado, para depois alterar o resto.*/
		Person entity = this.personRepository.findById(
			person.getId()).orElseThrow(() -> new 
				ResourceNotFoundException(
					"No records found for this ID"));
		/*Os outros parametros podemos alterar*/
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAdress(person.getAdress());
		entity.setGender(person.getGender());
		return this.personRepository.save(entity);
	}
	
	public void delete(Long id) 
	{	Person person = this.personRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException(
					"No records found for this ID"));
		if(person != null) this.personRepository.delete(person);
	}

	public Person findById(Long id)
	{	return this.personRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException(
					"No records found for this ID"));
	}
	
	public List<Person> findAll() 
	{return this.personRepository.findAll();}
}