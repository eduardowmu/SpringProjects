package br.com.erudio.restwithspringboot.data.vo;

import java.io.Serializable;
import org.springframework.hateoas.RepresentationModel;

import com.github.dozermapper.core.Mapping;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/*Para que não mudemos a forma de como devemos
 *receber os parametros, colocamos-nos novamente
 *desse jeito, e assim não importa se algum outro 
 *desenvolvedor alterar a ordem de como os atributos
 *estão distribuídos*/
@JsonPropertyOrder({
	"id",
	"firstName",
	"lastName",
	"adress",
	"gender"})
public class PersonVO extends RepresentationModel 
	implements Serializable
{	private static final long serialVersionUID = 1L;
	/*Notation do DOZER, que desse jieto, este irá conseguir se achar*/
	@Mapping("id")
	@JsonProperty("id")
	private Long key;
	/*Se quisésemos que tanto firstName como lastName fossem separados não como CamelCase
	 *mas sim por underline, podemos também usar a mesma notação acima, que obviamente nos
	 *forçaria mudar também no JsonPropertyOrder acima*/
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("lastName")
	private String lastName;
	private String adress;

	/*Se quisessemos que este atributo não aparecesse*/
	//@JsonIgnore
	@JsonProperty("gender")
	private String gender;
	
	public PersonVO() {}
	
	public Long getKey() {return key;}
	public void setKey(Long key) {this.key = key;}

	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}
	
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}
	
	public String getAdress() {return adress;}
	public void setAdress(String adress) {this.adress = adress;}
	
	public String getGender() {return gender;}
	public void setGender(String gender) {this.gender = gender;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{	if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		PersonVO other = (PersonVO) obj;
		if (adress == null) 
		{	if (other.adress != null)
				return false;
		} else if (!adress.equals(other.adress))
			return false;
		if (firstName == null) 
		{	if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) 
		{	if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (key == null) 
		{	if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (lastName == null) 
		{	if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
}