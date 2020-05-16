package br.edu.fatec.topicos.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor						
@NoArgsConstructor
@SuppressWarnings("serial")//ignora a necessidade da variável serial.
@MappedSuperclass //para dizer ao JPA que esta é uma superclasse das entidades que iremos implementar
public abstract class Person 
{	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "Name is necessary!")
	@Column(nullable=false)
	private String name;
	
	@NotBlank(message = "CPF is necessary!")
	@Column(nullable=false)
	private String cpf;
	
	@NotNull
	@DateTimeFormat(iso = ISO.DATE)
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate bdate;
	
	@NotBlank(message = "Email is necessary!")
	@Column(nullable=false)
	private String email;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "adr_id")//chave estrangeira
	private Adress adress;
}