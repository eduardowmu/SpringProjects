package br.com.erudio.restwithspringbootudemy.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.restwithspringbootudemy.model.Person;
import br.com.erudio.restwithspringbootudemy.service.PersonService;

@RestController
@RequestMapping("/person")
public class PersonController 
{	@Autowired private PersonService personService;
	
	@RequestMapping(method=RequestMethod.POST,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Person create(@RequestBody Person person)
	{return this.personService.create(person);}
	
	@RequestMapping(method=RequestMethod.PUT,
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Person update(@RequestBody Person person)
	{return this.personService.update(person);}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id)	
	{this.personService.delete(id);}
	
	@RequestMapping(value = "/{id}", method=RequestMethod.GET, 
		produces=MediaType.APPLICATION_JSON_VALUE)
	public Person findById(@PathVariable("id") Long id)	
	{return this.personService.findById(id);}
	
	@RequestMapping(method=RequestMethod.GET, 
		produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() {return this.personService.findAll();}
}