package com.edu.demoajax.web.domain;

import java.io.Serializable;
//como os dados irão trafegar na rede, é sempre bom ter a
//classe que implementa esta interface, pois esta vai garantir
//que os dados que estão saindo serão exatamente os dados que 
//estão chegando do outro lado.
@SuppressWarnings("serial")//Como estamos implementando esta interface, a idéia é ficar pedindo para que gere um ID referente a esta implementação
public class SocialMetaTag implements Serializable
{	//este atributo vai servir para que armazenemos a instrução 
	//referente ao nome do site da URL que estamos acessando o produto
	//que está tendo cadastrado 
	private String site;
	
	//para armazenar o titulo do produto que está sendo cadastrado na app
	private String title;
	
	//atributo para armazenar a URL referente à promoção 
	private String url;
	
	//atributo para armazenar a imagem, ou melhor, a URL de acesso desta imagem
	private String image;

	public String getSite() {return site;}
	public void setSite(String site) {this.site = site;}

	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}

	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}

	public String getImage() {return image;}
	public void setImage(String image) {this.image = image;}
	
	@Override public String toString() 
	{return "SocialMetaTag [site=" + site + ", title=" + title + ", url=" + url + ", image=" + image + "]";}
}