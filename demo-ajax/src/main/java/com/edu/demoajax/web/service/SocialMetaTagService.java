package com.edu.demoajax.web.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import com.edu.demoajax.web.domain.SocialMetaTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//notação que transforma a classe como de serviço, gerenciado pelo Bean validation
@Service//Este método serve para recuperar as partes mais importantes dos metatags do OpenGraph
public class SocialMetaTagService//o parâmetro será a url de acesso
{	private static Logger log = LoggerFactory.getLogger(SocialMetaTagService.class);

	public SocialMetaTag getSocialMetaTagByURL(String url)
	{	SocialMetaTag twitter = this.getTwitterCardByURL(url);
		if(!this.isEmpty(twitter))	return twitter;
		
		SocialMetaTag opg = this.getOpenGraphByURL(url);
		if(!this.isEmpty(opg))	return opg;
		
		/*o retorno nulo será tratado na controller para enviar uma msg
		 ao usuário dizendo que não foi possível recuperar os dados da URL
		 que está tentando cadastrar na aplicação*/
		return null;
	}
	
	private SocialMetaTag getOpenGraphByURL(String url)
	{	SocialMetaTag tag = new SocialMetaTag();
		//no JSOUP armazenamos os dados recuperados em um objeto do tipo document
		try //é necessário ler toda a documentação do JSOUP
		{	Document doc = Jsoup.connect(url).get();
			//neste SETTER estou indicando o que quero recuperar na página em HTML
			//o metodo attr() faz a indicação de qual atributo da meta tag que queremos recuperar.
			//para o conteúdo. 
			tag.setTitle(doc.head().select("meta[property=og:title]").attr("content"));
			tag.setSite(doc.head().select("meta[property=og:site_name]").attr("content"));
			tag.setImage(doc.head().select("meta[property=og:image]").attr("content"));
			tag.setUrl(doc.head().select("meta[property=og:url]").attr("content"));
		} 
		catch (IOException e) 
		{	System.out.println(e.getMessage());
			log.error(e.getMessage(), e.getCause());
		}
		return tag;
	}
	/*Quando não encontramos meta tags usando o OpenGraph, devemos fazer testes com Twitter card*/
	private SocialMetaTag getTwitterCardByURL(String url)
	{	SocialMetaTag tag = new SocialMetaTag();
		//no JSOUP armazenamos os dados recuperados em um objeto do tipo document
		try //é necessário ler toda a documentação do JSOUP
		{	Document doc = Jsoup.connect(url).get();
			//neste SETTER estou indicando o que quero recuperar na página em HTML
			//o metodo attr() faz a indicação de qual atributo da meta tag que queremos recuperar.
			//para o conteúdo. 
			tag.setTitle(doc.head().select("meta[name=twitter:title]").attr("content"));
			tag.setSite(doc.head().select("meta[name=twitter:site]").attr("content"));
			tag.setImage(doc.head().select("meta[name=twitter:image]").attr("content"));
			tag.setUrl(doc.head().select("meta[name=twitter:url]").attr("content"));
		} 
		catch (IOException e) 
		{	System.out.println(e.getMessage());
			log.error(e.getMessage(), e.getCause());
		}
		return tag;
	}
	
	/*método booleano que verifica se por um método não retornar as metatags, 
	 devemos então usar por outro*/
	private boolean isEmpty(SocialMetaTag tag)
	{	if(tag.getImage().isEmpty())	return true;
		if(tag.getSite().isEmpty())	return true;
		if(tag.getTitle().isEmpty())	return true;
		if(tag.getUrl().isEmpty())	return true;
		/*se houver metatag do tipo OpenGraph, então continuará usando esta 
		 Social Meta Tag*/
		return false;
	}
}