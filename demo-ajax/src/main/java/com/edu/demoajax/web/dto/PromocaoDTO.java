package com.edu.demoajax.web.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

import com.edu.demoajax.web.domain.Categoria;

//DTO significa Data Transfer Object, que é um design pattern muito utilizado
//em aplicações para quando vc precisa modificar os dados que irá transitar entre o cliente
//e o servidor. Alguns dos campos que nao temos no DTO e temos em promocao são o campo
//link de promocao, o campo site, likes, e dtCadastro. 
public class PromocaoDTO 
{	@NotNull private Long id;
	
	@NotBlank(message="É necessário um titulo") 
	private String titulo;
	
	private String descricao;
	
	@NotBlank(message="É necessário uma imagem") 
	private String linkImagem;
	
	@NotNull(message="O preço é necessário")
	@NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
	private BigDecimal preco;
	
	@NotNull(message="Uma categoria é obrigatório")
	private Categoria categoria;

	public Long getId() {return id;}
	public void setId(Long id) {this.id = id;}

	public String getTitulo() {return titulo;}
	public void setTitulo(String titulo) {this.titulo = titulo;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public String getLinkImagem() {return linkImagem;}
	public void setLinkImagem(String linkImagem) {this.linkImagem = linkImagem;}

	public BigDecimal getPreco() {return preco;}
	public void setPreco(BigDecimal preco) {this.preco = preco;}

	public Categoria getCategoria() {return categoria;}
	public void setCategoria(Categoria categoria) {this.categoria = categoria;}
}