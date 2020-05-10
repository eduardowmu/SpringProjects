package com.edu.demoajax.web.service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.edu.demoajax.web.domain.Promocao;
import com.edu.demoajax.web.repository.PromocaoRepository;

/*Classe onde iremos trabalhar com as regras de negócio dos Data tables*/
public class PromocaoDataTablesService 
{	//nome dos atributos que temos na classe promoção e estes atributos
	//devem seguir exatamente a mesma órdem que temos das colunas da página
	//HTML como também das colunas que foram declaradas no arquivo promo-datatable.js
	//este vetor é necessário pois temos que relacionar as colunas que tem na tabela
	//com os atributos que temos na classe de promoção que representam então as colunas
	//que temos no BD 
	private String[] cols = {"id", "titulo", "site", "linkPromocao", "descricao", 
							 "linkImagem", "preco", "likes", "dtCadastro", "categoria"};
	
	//o parametro repository é para acessar a camada de persistência e o request é para que
	//possa recuperar as informações que são enviadas pelo cliente aqui pro lado servidor 
	public Map<String, Object> execute(PromocaoRepository repository, HttpServletRequest request)
	{	//o parametro start é o parametro que indica no datatables que indica o primeiro ponto
		//de partida do dataset atual (datatables.net/manual/server-side)
		int start = Integer.parseInt(request.getParameter("start"));
		
		//segunda variavel também do tipo int que será a qtdade de itens que teremos por página
		//na tabela. este parametro também podemos pegar no site datatables.net
		int length = Integer.parseInt(request.getParameter("length"));
		
		//parametro que será incrementado a cada requisição. quando vc abre a tabela abre por exemplo
		//com valor 1. Quando fazemos uma paginação passa a ser valor seguinte e assim por diante. É
		//uma forma que o datatable utiliza para segurança da transação dos dados.
		int draw = Integer.parseInt(request.getParameter("draw"));
		
		//iremos recuperar a partir de um método que vamos criar
		int current = this.currentPage(start, length);
		
		//metodo que nos retorna o valor da coluna
		String column = columnName(request);
		
		//metodo responsavel pela ordenação, se é acendente ou descendente
		Sort.Direction direction = orderBy(request);
		
		//variavel do tipo string que será o valor do campo de pesquisa
		String search = searchBy(request);
		
		//regras de paginação
		Pageable pageable = PageRequest.of(current, length, direction, column);
		
		//variavel do tipo page
		Page<Promocao> page = queryBy(search, repository, pageable);
		
		//variavel do tipo Map
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw);
		//total de consultas tem antes de realizar a paginação
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		//o método getComtent() tem como retorno uma lista de promoções
		json.put("data", page.getContent());
		return json;
	}
	
	//este método será util para popular a tabela, realizar a paginação, fazer a ordenação
	//pelas colunas e também selecionar a quantidade de itens que deseja exibir por página
	//na tabela.
	private Page<Promocao> queryBy(String search, PromocaoRepository repository, Pageable pageable) 
	{	//caso o campo de pesquisa estiver vazia, retorne tudo
		if(search.isEmpty()) {return repository.findAll(pageable);}
		
		//inclusão de um teste para verificar se a consulta esta sendo feita pelo preço. O
		//Método matches cria uma expressão regular que testa se o valor digitado no campo
		//de input de pesquisa é um valor monetário. Na verdade verifica se os caracteres iniciais
		//digitados é um digito de 0-9. Caso contrário, não cairá nesta condição. Depois é
		//verificado se possui um ponto ou vírgula, que podem caracterizar um digito monetário.
		//Depois verifica os digitos decimais que representariam numeros dos centavos, em até dois digitos
		//referencia: https://regex101.com
		if(search.matches("^[0-9]+([.,][0-9]{2})?$"))
		{	//quando formos transformar nossa variável search em um objeto BigDecimal para enviar este
			//para consulta, se enviarmos a separação dos centavos com uma vírgula teremos uma exceção.
			search = search.replace(",", ".");
			return repository.findByPrice(new BigDecimal(search), pageable);
		}
		
		//caso contrário retorna apenas o que estiver na pesquisa
		return repository.findByTitleOrSiteOrCategory(search, pageable);
	}

	private String searchBy(HttpServletRequest request) 
	{	//parâmetro do name do campo de pesquisa. Se estiver vazia, retorna vazio,
		//se não retorna o proprio valor do campo.
		return request.getParameter("search[value]").isEmpty()
				? "" : request.getParameter("search[value]");
	}
	
	private Direction orderBy(HttpServletRequest request) 
	{	String order =  request.getParameter("order[0][dir]");
		//define que será ascendente
		Sort.Direction sort = Sort.Direction.ASC;
		//testa se o valor dessa variável é a palavra descendente (desc).
		//se for, será do tipo descendente
		if(order.equalsIgnoreCase("desc"))	{sort = Sort.Direction.DESC;}
		return sort;
	}
	
	private String columnName(HttpServletRequest request) 
	{	//parametros que buscamos também em datatables.net, referente ao calor da coluna
		int iCol = Integer.parseInt(request.getParameter("order[0][column]"));
		return this.cols[iCol];
	}
	
	//os parametros sao os que recuperamnos do request
	private int currentPage(int start, int length)	{return length > 0 ? start/length:0;}
}

