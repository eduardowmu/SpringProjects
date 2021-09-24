package br.com.erudio.restwithspringboot.convertertest;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.erudio.restwithspringboot.converter.DozerConverter;
import br.com.erudio.restwithspringboot.converter.mocks.MockPerson;
import br.com.erudio.restwithspringboot.data.vo.PersonVO;
import br.com.erudio.restwithspringboot.model.Person;
import junit.framework.Assert;

public class DozerConverterTest 
{	MockPerson inputObject;
	
	@Before
	public void setUp() {this.inputObject = new MockPerson();}
	
	@Test
	public void parseEntityToVOTest()
	{	PersonVO output = DozerConverter.parseObject(this.inputObject.mockEntity(), PersonVO.class);
		
		Assert.assertEquals(Long.valueOf(0L), output.getKey());
		Assert.assertEquals("First Name Teste 0", output.getFirstName());
		Assert.assertEquals("Last Name Teste 0", output.getLastName());
		Assert.assertEquals("Adress Teste 0", output.getAdress());
		Assert.assertEquals("Gender Teste 0", output.getGender());
	}
	
	@Test
	public void parseEntityListToVOListTest()
	{	List<PersonVO> outputList = DozerConverter.parseListObjects(this.inputObject.mockEntityList(), PersonVO.class);
		PersonVO outPutZero = outputList.get(0);
		
		Assert.assertEquals(Long.valueOf(0L), outPutZero.getKey());
		Assert.assertEquals("First Name Teste 0", outPutZero.getFirstName());
		Assert.assertEquals("Last Name Teste 0", outPutZero.getLastName());
		Assert.assertEquals("Adress Teste 0", outPutZero.getAdress());
		Assert.assertEquals("Gender Teste 0", outPutZero.getGender());
		
		PersonVO outPutSeven = outputList.get(7);
		Assert.assertEquals(Long.valueOf(0L), outPutSeven.getKey());
		Assert.assertEquals("First Name Teste 7", outPutSeven.getFirstName());
		Assert.assertEquals("Last Name Teste 7", outPutSeven.getLastName());
		Assert.assertEquals("Adress Teste 7", outPutSeven.getAdress());
		Assert.assertEquals("Gender Teste 7", outPutSeven.getGender());
		
		PersonVO outPutTwelve = outputList.get(12);
		Assert.assertEquals(Long.valueOf(0L), outPutTwelve.getKey());
		Assert.assertEquals("First Name Teste 7", outPutTwelve.getFirstName());
		Assert.assertEquals("Last Name Teste 7", outPutTwelve.getLastName());
		Assert.assertEquals("Adress Teste 7", outPutTwelve.getAdress());
		Assert.assertEquals("Gender Teste 7", outPutTwelve.getGender());
	}
	
	@Test
	public void parseVOToEntityTest()
	{	Person output = DozerConverter.parseObject(this.inputObject.mockVO(), Person.class);
		
		Assert.assertEquals(Long.valueOf(0L), output.getId());
		Assert.assertEquals("First Name Teste 0", output.getFirstName());
		Assert.assertEquals("Last Name Teste 0", output.getLastName());
		Assert.assertEquals("Adress Teste 0", output.getAdress());
		Assert.assertEquals("Gender Teste 0", output.getGender());
	}
	
	@Test
	public void parserVOListToEntityListTest()
	{	List<Person> outputList = DozerConverter.parseListObjects(
			this.inputObject.mockVOList(), Person.class);
		Person outPutZero = outputList.get(0);
		Assert.assertEquals(Long.valueOf(0L), outPutZero.getId());
		Assert.assertEquals("First Name Teste 0", outPutZero.getFirstName());
		Assert.assertEquals("Last Name Teste 0", outPutZero.getLastName());
		Assert.assertEquals("Adress Teste 0", outPutZero.getAdress());
		Assert.assertEquals("Gender Teste 0", outPutZero.getGender());
		
		Person outPutSeven = outputList.get(7);
		Assert.assertEquals(Long.valueOf(0L), outPutSeven.getId());
		Assert.assertEquals("First Name Teste 7", outPutSeven.getFirstName());
		Assert.assertEquals("Last Name Teste 7", outPutSeven.getLastName());
		Assert.assertEquals("Adress Teste 7", outPutSeven.getAdress());
		Assert.assertEquals("Gender Teste 7", outPutSeven.getGender());
		
		Person outPutTwelve = outputList.get(12);
		Assert.assertEquals(Long.valueOf(0L), outPutTwelve.getId());
		Assert.assertEquals("First Name Teste 7", outPutTwelve.getFirstName());
		Assert.assertEquals("Last Name Teste 7", outPutTwelve.getLastName());
		Assert.assertEquals("Adress Teste 7", outPutTwelve.getAdress());
		Assert.assertEquals("Gender Teste 7", outPutTwelve.getGender());
	}
}