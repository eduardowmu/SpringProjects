package com.edu.exemplo.boot.domain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "endereco")
public class Endereco extends AbstractEntity<Long> 
{	@NotBlank
	@Size(min=3, max=255)
	@Column(name = "logradouro", nullable = false)
	private String logradouro;
	
	@NotBlank
	@Size(min=3, max=255)
	@Column(name = "bairro", nullable = false)
	private String bairro;
	
	@NotBlank
	@Size(min=3, max=255)
	@Column(name = "cidade", length = 500, nullable = false)
	private String cidade;
	
	@NotNull(message="{NotNull.endereco.uf}")
	@Column(name="uf", nullable = false, length = 2)
	@Enumerated(EnumType.STRING)//esta anotação informa ao JPA qual é o tipo de dado que queremos que salve no BD
	private UF uf;
	
	@NotBlank
	@Size(min=9, max=9, message="{Size.endereco.cep}")
	@Column(name = "cep", length = 9, nullable = false)
	private String cep;
	
	//se for inserido um digito de tipo alfabetico, então esta notação irá
	//invalidar o campo numero
	@NotNull(message="{NotNull.endereco.numero}")
	@Digits(integer=5, fraction=0)
	@Column(name = "numero", length = 5, nullable = false)
	private Integer numero;
	
	
	@Column(name = "complemento", length = 500, nullable = true)
	private String complemento;

	public String getLogradouro() {return logradouro;}
	public void setLogradouro(String logradouro) {this.logradouro = logradouro;}

	public String getBairro() {return bairro;}
	public void setBairro(String bairro) {this.bairro = bairro;}
	
	public String getCidade() {return cidade;}
	public void setCidade(String cidade) {this.cidade = cidade;}

	public UF getUf() {return uf;}
	public void setUf(UF uf) {this.uf = uf;}

	public String getCep() {return cep;}
	public void setCep(String cep) {this.cep = cep;}

	public Integer getNumero() {return numero;}
	public void setNumero(Integer numero) {this.numero = numero;}

	public String getComplemento() {return complemento;}
	public void setComplemento(String complemento) {this.complemento = complemento;}
}