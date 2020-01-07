package com.edu.exemplo.boot.domain;
import java.util.List;

//a notação @Entity permite que esta entidade seja gerenciada pela JPA
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//uma classe herdeira de AbstractEntity, com ID do tipo Long
@SuppressWarnings("serial")
@Entity
@Table(name="departamento")//define o nome da tabela no BD
public class Departamento extends AbstractEntity<Long> 
{	@NotBlank(message = "Informe um nome")//notação de validação para caso tenha campo de entrada em branco ou apenas preenchida com espaços em branco
	//notação de tamanho
	@Size(min = 3, max = 80, message="O departamento deve conter entre {min} e {max} caracteres")
	//atributo nome na tabela não podendo ser nulo e será única 
	//(sem repetição) e será um tipo VARCHAR de no máximo 60 caracteres.
	@Column(name="nome", nullable=false, unique = true, length = 60)
	private String nome;
	
	//assim como foi mapeado uma chave estrangeira para a entidade cargo,
	//temos um relacionamento bi direcional, e quado temos isso, devemos 
	//definir qual é o lado fraco e lado forte da relação, que neste caso
	//é esta classe possui o lado forte.
	@OneToMany(mappedBy = "departamento")
	private List<Cargo> cargos;

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public List<Cargo> getCargos() {return cargos;}
	public void setCargos(List<Cargo> cargos) {this.cargos = cargos;}
}