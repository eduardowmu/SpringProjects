package br.edu.fatec.topicos.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter							
@AllArgsConstructor						
@NoArgsConstructor
@Entity
public class Usuario 
{	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 50)
	private String login;
	
	@Column(length = 200)
	private String senha;
	
	//cascade = CascadeType.ALL faz com que, quando salva ou delete um usuário, faça para todos os seus papéis
	//fetch = FetchType.ALL, faz com que quando consulto os dados do usuário, mostre todos os seus papéis.
	@ManyToMany
	private Set<Papel> papeis;
}
