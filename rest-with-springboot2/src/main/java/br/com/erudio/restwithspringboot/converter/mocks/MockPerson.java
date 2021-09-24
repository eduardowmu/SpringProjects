package br.com.erudio.restwithspringboot.converter.mocks;
import java.util.ArrayList;
import java.util.List;

import br.com.erudio.restwithspringboot.data.vo.PersonVO;
import br.com.erudio.restwithspringboot.model.Person;

/*Essa classe testa se a conversão está funcionando corretamente*/
public class MockPerson 
{	public Person mockEntity()
	{return mockEntity(0);}

	public PersonVO mockVO()
	{return mockVO(0);}
	
	public List<Person> mockEntityList()
	{	List<Person> persons = new ArrayList<>();
		for(int i = 0; i < 14; i++)
		{persons.add(mockEntity(i));}
		return persons;
	}
	
	private Person mockEntity(Integer number)
	{	Person person = new Person();
		person.setAdress("Adresses Test " + number);
		person.setFirstName("First Name Teste " + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setId(number.longValue());
		person.setLastName("Last Name Teste " + number);
		return person;
	}
	
	private PersonVO mockVO(Integer number)
	{	PersonVO person = new PersonVO();
		person.setAdress("Adresses Test " + number);
		person.setFirstName("First Name Teste " + number);
		person.setGender(((number % 2) == 0) ? "Male" : "Female");
		person.setKey(number.longValue());
		person.setLastName("Last Name Teste " + number);
		return person;
	}

    public List<PersonVO> mockVOList() 
	{	List<PersonVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {persons.add(mockVO(i));}
        return persons;
    }
}
