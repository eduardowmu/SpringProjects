package br.edu.fatec2020.topicos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.format.annotation.NumberFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter							//notação do lombok que permite criar todos os métodos GETTERs e SETTERs
@AllArgsConstructor						//cria um construtor que recebe todos os parâmetros
@NoArgsConstructor						//cria um construtor sem parametros
@Entity
public class Produto
{	@Id @GeneratedValue(strategy=GenerationType.AUTO)					//a segunda serve para indicar que é um chave primária e que será gerado um valor automaticamente				
	private Long id;					
	
	@NotBlank(message = "Nome é obrigatório")
	@Column(name="name", nullable=false)
	private String name;
	
	@NotBlank(message = "Descriçao é obrigatório")
	@Column(name="description", nullable=false)
	private String description;
	
	@NotNull(message="Preço é obrigatório")
	@NumberFormat(style=Style.CURRENCY ,pattern="#,##0.00")
	@Column(name="price", nullable=false)
	private BigDecimal price;
}
