package br.com.erudio.restwithspringboot.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/*Notação do hibernate para que reconheça um objeto desse tipo como entidade que deve
 *persistir no banco de dados, porém a notação que devemos importar é do javax persistence*/
@Entity
/*Para que reconheça como uma tabela*/
@Table(name="person")
public class Person implements Serializable
{	private static final long serialVersionUID = 1L;
	
	/*Colunas*/
	@Id/*A notação abaixo indica que o hibernate se encarregaará de gerar o ID*/
	@GeneratedValue(strategy=GenerationType.IDENTITY)//com autoincremente
	private Long id;
	
	@Column(name="first_name", nullable=false, length=100)
	private String firstName;
	
	@Column(name="last_name", nullable=false, length=100)
	private String lastName;
	
	@Column(nullable=false, length=250)
	private String adress;
	
	/*Genero não pode ser nulo e no maximo com 6 caractere (male/female)*/
	@Column(nullable=false, length=10)
	private String gender;
	
	public Person() {}

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}

	public String getLastName() {return lastName;}
	public void setLastName(String lastName) {this.lastName = lastName;}

	public String getAdress() {return adress;}
	public void setAdress(String adress) {this.adress = adress;}

	public String getGender() {return gender;}
	public void setGender(String gender) {this.gender = gender;}

	@Override
	public int hashCode() 
	{	final int prime = 31;
		int result = 1;
		result = prime * result + ((adress == null) ? 0 : adress.hashCode());
		
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) 
	{	if (this == obj)	return true;
		if (obj == null)	return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (adress == null) 
		{	if (other.adress != null)
				return false;
		} 
		else if (!adress.equals(other.adress))
			return false;
		if (firstName == null) 
		{	if (other.firstName != null)
				return false;
		} 
		else if (!firstName.equals(other.firstName))
			return false;
		if (gender == null) 
		{	if (other.gender != null)
				return false;
		} 
		else if (!gender.equals(other.gender))
			return false;
		if (id == null) 
		{	if (other.id != null)
				return false;
		} 
		else if (!id.equals(other.id))
			return false;
		if (lastName == null) 
		{	if (other.lastName != null)
				return false;
		} 
		else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}
}