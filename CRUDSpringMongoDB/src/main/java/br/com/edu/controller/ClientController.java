package br.com.edu.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edu.domain.Client;
import br.com.edu.responses.Response;
import br.com.edu.serviceImp.ClientService;
//This notation allows any class be a Rest type
@RestController//this path will be our app base path, it means that it will be
@RequestMapping(path="/api/clients")//our URL
public class ClientController 
{	@Autowired private ClientService service;
	
	//método que usará e será feito dentro de um objeto próprio do Spring
	//da classe chamado ResponseEntity, que ajuda a retornar os dados da
	//da comunicação. Está definido como um tipo GET e não será definido um
	//path, pois estamos assumindo que o path do RequestMapping será usado
	//como padrão para retornar essa informação.
	@GetMapping public ResponseEntity<Response<List<Client>>> listAllClient()
	{return ResponseEntity.ok(new Response<List<Client>>(this.service.listAll()));}
	
	//como será feita listagem pelo ID, entã será criado um path. As chaves indicam
	//que o id é um valor dinâmico.e estará na URL. Toda vez que fizer uma requisição
	//com "/api/clientes/id", será usado este {id} 
	@GetMapping(path="/{id}") public ResponseEntity<Response<Client>> listClientID(@PathVariable(name="id") String id)
	{return ResponseEntity.ok(new Response<Client>(this.service.searchClientID(id)));}
	
	//o @RequestBody faz com que o spring extraia automaticamente dados vindo
	//do PostRequest e criará um objeto do tipo Client automaticamente para nós
	//a notação "@Valid" é a notação responsável pelas validações.
	//o BindResult é a classe que me retorna o resultado da aplicação.
	@PostMapping public ResponseEntity<Response<Client>> save(@Valid @RequestBody Client client, BindingResult result)
	{	if(result.hasErrors())//se houver erros
		{	List<String> errors = new ArrayList<String>();
			//insere numa lista de erros as mensagens de erros que podem aparecer
			result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			//um erro comum de tipo 400. Como deve ser retornado um tipo Client, devemos
			//criar uma classe que permita retornar uma mensagem de resposta papdrão
			return ResponseEntity.badRequest().body(new Response<Client>(errors));
		}
		return ResponseEntity.ok(new Response<Client>(this.service.save(client)));
	}
	
	//lembrando que nesses casos o objeto cliente possui um id
	@PutMapping(path="/{id}") public ResponseEntity<Response<Client>> update(@Valid @PathVariable(name="id") String id, @RequestBody Client client, BindingResult result)
	{	if(result.hasErrors())//se houver erros
		{	List<String> errors = new ArrayList<>();
			//insere numa lista de erros as mensagens de erros que podem aparecer
			result.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));
			//um erro comum de tipo 400. Como deve ser retornado um tipo Client, devemos
			//criar uma classe que permita retornar uma mensagem de resposta papdrão
			return ResponseEntity.badRequest().body(new Response<Client>(errors));
		}
		client.setId(id);
		return ResponseEntity.ok(new Response<Client>(this.service.update(client)));
	}
	
	@DeleteMapping(path="/{id}")
	public ResponseEntity<Response<Integer>> delete(@PathVariable(name="id") String id)
	{	this.service.delete(id);
		//retorna um inteiro apenas para indicar que está tudo ok
		return ResponseEntity.ok(new Response<Integer>(1));
	}
}