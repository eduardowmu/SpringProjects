package com.edu.exemplo.boot.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

@SuppressWarnings("serial")
@Entity
@Table(name = "funcionario")
public class Funcionario extends AbstractEntity<Long> 
{	@NotBlank//notação de validação para campos não vazias ou prenchimento apenas com espaços
	@Size(max=255, min=3)
	@Column(name="nome", length = 500, nullable = false)
	private String nome;
	
	@NotNull
	//notação para formatação de numeros, do Spring
	@NumberFormat(style = Style.CURRENCY, pattern="#,##0.00")
	//"columnDefinition" atributo que serve para definir qual o tipo de dado de pto flutuante e seu default
	@Column(name = "salario", nullable = false, columnDefinition = "DECIMAL(9,2) DEFAULT 0.00")
	private BigDecimal salario;
	
	@NotNull//compara com a data de hoje. se for maior, a validação não seria possível
	@PastOrPresent(message="{PastOrPresent.funcionario.dataEntrada}")
	//este DATE só serve para datas com dia, mes e ano. Existem outros tipos, que
	@DateTimeFormat(iso = ISO.DATE)//contém horas junto e outro com apenas tmepo em horas
	@Column(name = "data_entrada", nullable = false, columnDefinition = "DATE")
	private LocalDate dataEntrada;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_saida", nullable = true, columnDefinition = "DATE")
	private LocalDate dataSaida;
	
	//quando utilizo essa notação sobre objeto endereço, queremos dizer que este objeto endereço deve ser validado,
	//conforme as instruções de validação que temos na classe de endereços.
	@Valid
	//essa notação permite que, além de mostrar ao hibernate que a relação é de 1 X 1, ao inserir um 
	//funcionario, será inserido também em cascata o endereço. e a mesma coisa acontece quando atualiza ou 
	//deleta um funcionario.
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fun_end_id")//chave estrangeira
	private Endereco endereco;
	
	@ManyToOne
	@JoinColumn(name = "fun_car_id")
	private Cargo cargo;

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public BigDecimal getSalario() {return salario;}
	public void setSalario(BigDecimal salario) {this.salario = salario;}

	public LocalDate getDataEntrada() {return dataEntrada;}
	public void setDataEntrada(LocalDate dataEntrada) {this.dataEntrada = dataEntrada;}

	public LocalDate getDataSaida() {return dataSaida;}
	public void setDataSaida(LocalDate dataSaida) {this.dataSaida = dataSaida;}

	public Endereco getEndereco() {return endereco;}
	public void setEndereco(Endereco endereco) {this.endereco = endereco;}

	public Cargo getCargo() {return cargo;}
	public void setCargo(Cargo cargo) {this.cargo = cargo;}
}
