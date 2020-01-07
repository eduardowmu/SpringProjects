package com.edu.exemplo.boot.domain;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "cargo")
public class Cargo extends AbstractEntity<Long> 
{	@NotBlank(message = "Insira um nome de cargo")
	@Size(max = 60, message = "O nome de cargo deve conter no máximo {max} caracteres")
	@Column(name="nome", nullable = false , unique=true, length = 60)
	private String nome;
	
	@NotNull(message = "Selecione o departamento do cargo")
	//notação para indicar um relacionamento de muitos para um, pois
	//sempre devemos ler da esquerda para a direita (um departamento
	//pode ter mais de um cargo). Como estamos na classe Cargo, consideramos
	//que esta entidade está a direita.
	@ManyToOne
	@JoinColumn(name = "car_dep_id")//nome da chave estrangeira
	private Departamento departamento;
	
	//notação de um para muitos e com variavel de mapeamento, e que diz que a
	//entidade cargo é o lado fraco do relacionamento e o lado forte fica para
	//funcionarios. Através disso quando for fazer uma consulta por um cargo, este
	//também irá trazer resultados de funcionarios.
	@OneToMany(mappedBy = "cargo")
	private List<Funcionario> funcionarios;

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public Departamento getDepartamento() {return departamento;}
	public void setDepartamento(Departamento departamento) {this.departamento = departamento;}
	
	public List<Funcionario> getFuncionarios() {return funcionarios;}
	public void setFuncionarios(List<Funcionario> funcionarios) {this.funcionarios = funcionarios;}
}