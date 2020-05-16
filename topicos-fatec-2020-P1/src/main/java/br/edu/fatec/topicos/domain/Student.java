package br.edu.fatec.topicos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter							
@AllArgsConstructor						
@NoArgsConstructor
@Entity
public class Student extends Person 
{	@NotBlank(message = "Enroll is necessary!")
	@Column(nullable=false)
	private String enrollNumber;
}
