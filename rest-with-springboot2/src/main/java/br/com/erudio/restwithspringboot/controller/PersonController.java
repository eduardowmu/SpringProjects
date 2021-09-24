package br.com.erudio.restwithspringboot.controller;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.restwithspringboot.data.vo.PersonVO;
//import br.com.erudio.restwithspringboot.data.vo.v2.PersonVOV2;
import br.com.erudio.restwithspringboot.service.PersonService;

/*Criação de uma versão inicial*/
@RestController/*Este contexto adiciona um autorelacionamento*/
@RequestMapping("/api/person/v1")
public class PersonController 
{	@Autowired private PersonService personService;

	/*O produces e consumes seguem a convenção o JSON. Portanto se não for especificado será produzido/consumido
	 *JSON. Portanto não precisamos inseri-las*/
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"}, consumes = {"application/json", "application/xml", "application/x-yaml"})
	public PersonVO create(@RequestBody PersonVO person)
	{	PersonVO personVO = this.personService.create(person);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		return personVO;
	}
	
	//nova versão
//	@PostMapping("/v2")
//	public PersonVOV2 createV2(@RequestBody PersonVOV2 person)
//	{return this.personService.createV2(person);}
	
	/*A mesma coisa quando queremos especificar o tipo da
	 *requisição, pois não precisamos do RequestMapping*/
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"}, consumes = {"application/json", "application/xml", "application/x-yaml"})
	public PersonVO update(@RequestBody PersonVO person)
	{	PersonVO personVO = this.personService.update(person);
		personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
		return personVO;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id)	
	{	this.personService.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value="/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
	public PersonVO findById(@PathVariable("id") Long id)	
	{	PersonVO personVO = this.personService.findById(id);
		/*Adicionando links para HATEOAS, de autorelacionamento*/
		personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return personVO;
	}
	
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
	public List<PersonVO> findAll() 
	{	List<PersonVO> personsVO = this.personService.findAll();
		personsVO.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		return personsVO;
	}
}
