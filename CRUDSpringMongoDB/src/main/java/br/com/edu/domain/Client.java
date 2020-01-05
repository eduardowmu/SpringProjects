package br.com.edu.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Client//atributos de classe
{	@Id private String id;
	private String nome;
	private String email;
	private String cpf;
	
	//constructor
	public Client() {}

	public String getId() {return id;}
	
	public void setId(String id) {this.id = id;}
	
	@NotEmpty(message="Nome não pode ser vazio")
	public String getNome() {return nome;}
	
	public void setNome(String nome) {this.nome = nome;}
	
	//validações para e-mail, vazio ou email valido
	@NotEmpty(message="Email não pode ser vazio")
	@Email(message="Email inválido!")
	public String getEmail() {return email;}
	
	public void setEmail(String email) {this.email = email;}
	
	@NotEmpty(message="CPF não pode ser vazio")
	@CPF(message="CPF inválido!")
	public String getCpf() {return cpf;}
	
	public void setCpf(String cpf) {this.cpf = cpf;}
	
	@Override public String toString() 
	{return "Client [id=" + id + ", nome=" + nome + ", email=" + email + ", cpf=" + cpf + "]";}
}