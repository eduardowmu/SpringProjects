package br.com.erudio.restwithspringboot.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import br.com.erudio.restwithspringboot.converter.DozerConverter;
import br.com.erudio.restwithspringboot.converter.PersonConverter;
import br.com.erudio.restwithspringboot.data.vo.PersonVO;
import br.com.erudio.restwithspringboot.data.vo.v2.PersonVOV2;
import br.com.erudio.restwithspringboot.model.Person;
import br.com.erudio.restwithspringboot.repository.PersonRepository;

@Service
public class PersonService implements Serializable
{	private static final long serialVersionUID = 1L;
	
	@Autowired PersonRepository personRepository;
	@Autowired PersonConverter converter;
	
	public PersonVO create(PersonVO person) 
	{	//RECURSO DO JAVA 11
		var entity = DozerConverter.parseObject(person, Person.class);
		
		var vo = DozerConverter.parseObject(this.personRepository.save(entity), PersonVO.class);
		
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) 
	{	//RECURSO DO JAVA 11
		var entity = converter.convertVOToEntity(person);
		
		var vo = converter.convertEntityToVO(this.personRepository.save(entity));
		
		return vo;
	}
	
	public PersonVO update(PersonVO person) 
	{	/*Busca pela entidade pelo ID, que não deve
	 	ser alterado, para depois alterar o resto.*/
		var entity = this.personRepository.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		/*Os outros parametros podemos alterar*/
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAdress(person.getAdress());
		entity.setGender(person.getGender());
		var vo = DozerConverter.parseObject(this.personRepository.save(entity), PersonVO.class);
		
		return vo;
	}
	
	public void delete(Long id) 
	{	Person person = this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		if(person != null) this.personRepository.delete(person);
	}

	public PersonVO findById(Long id)
	{	var entity = this.personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
	
		return DozerConverter.parseObject(entity, PersonVO.class);
	}
	
	public List<PersonVO> findAll() {return DozerConverter.parseListObjects(this.personRepository.findAll(), PersonVO.class);}
}
