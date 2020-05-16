package br.edu.fatec.topicos.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter							
@AllArgsConstructor						
@NoArgsConstructor
@Entity
public class Adress 
{	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "Street is necessary!")
	@Column(nullable=false)
	private String street;
	
	@Column(nullable=true)
	private Long number;
	
	@NotBlank(message = "Village is necessary!")
	@Column(nullable=false)
	private String village;
	
	@Column(nullable=false)
	private String state;
}