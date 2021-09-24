package br.com.erudio.restwithspringboot.converter;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.erudio.restwithspringboot.data.vo.v2.PersonVOV2;
import br.com.erudio.restwithspringboot.model.Person;

@Service
public class PersonConverter 
{	public PersonVOV2 convertEntityToVO(Person person)
	{	PersonVOV2 vo = new PersonVOV2();
		vo.setId(person.getId());
		vo.setAdress(person.getAdress());
		vo.setFirstName(person.getFirstName());
		vo.setLastName(person.getLastName());
		vo.setGender(person.getGender());
		vo.setBirthday(new Date());
		return vo;
	}

	public Person convertVOToEntity(PersonVOV2 person)
	{	Person entity = new Person();
		entity.setId(person.getId());
		entity.setAdress(person.getAdress());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		return entity;
	}
}