package br.edu.fatec2020.topicos.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter							//notação do lombok que permite criar todos os métodos GETTERs e SETTERs
@AllArgsConstructor						//cria um construtor que recebe todos os parâmetros
@NoArgsConstructor						//cria um construtor sem parametros
@Entity
public class Produto
{	@Id @GeneratedValue					//a segunda serve para indicar que é um chave primária e que será gerado um valor automaticamente				
	private Long id;					
	private String name;
	private String description;
	private float price;
}
