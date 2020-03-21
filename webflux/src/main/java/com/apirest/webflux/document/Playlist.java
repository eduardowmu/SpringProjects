package com.apirest.webflux.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
/* Anotação que fará o mapeamento entre o objeto Playlist para os dados play list no Banco de dados MongoDB 
 * (mapeamento Objeto X Dado). Quando ja usamos essa notação, podemos comaeçar a inserir esses dados no BD
 * Irá gerar automaticamente a coleção no nosso BD. Ao executar nosso App e Inserir alguns dados, irá criar
 * Automaticamente essa playlist no Atlas.*/
@Document
public class Playlist 
{	@Id
	private String id;
	private String nome;
	
	public Playlist(String id, String nome) 
	{	this.id = id;
		this.nome = nome;
	}
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
}