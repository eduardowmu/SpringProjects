package com.edu.demoajax.web.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name="categorias")
public class Categoria implements Serializable
{	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//este atributo, unique, funciona como chave primária em BD
	@Column(name="titulo", nullable=false, unique=true)
	private String titulo;
	
	//no modelo entidade e relacionamento, uma categoria pode ter
	//várias promoções. O parâmetro será o atributo que iremos cadastrar
	//na classe promoção, para que a JPA consiga encontrar o mapeamento.
	@OneToMany(mappedBy="categoria")
	private List<Promocao> promocoes;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getTitulo() {return titulo;}
	public void setTitulo(String titulo) {this.titulo = titulo;}

	//esta lista é que esta dando problemas, pois vamos supor que somos a biblioteca
	//Jackson no momento que vamos realizar a conversão do objeto java que vai ser
	//enviado como resposta em um objeto json. Quando a biblioteca Jackson for converter
	//a classe categoria em um objeto json, esta ignore a lista de promoções. Como
	//não precisamos que na tabela o objeto de categoria tenha a lista de promoções
	//então não será um efeito negativo para o nosso sistema.
	@JsonIgnore
	public List<Promocao> getPromocoes() {return promocoes;}
	public void setPromocoes(List<Promocao> promocoes) {this.promocoes = promocoes;}
	
	@Override public String toString() {return "Categoria [id=" + id + ", titulo=" + titulo + "]";}
}