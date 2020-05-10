package com.edu.demoajax.web.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.demoajax.web.domain.Categoria;
import com.edu.demoajax.web.domain.Promocao;
import com.edu.demoajax.web.dto.PromocaoDTO;
import com.edu.demoajax.web.repository.CategoriaRepository;
import com.edu.demoajax.web.repository.PromocaoRepository;
import com.edu.demoajax.web.service.PromocaoDataTablesService;

@Controller
@RequestMapping("/promocao")
public class PromocoesCotroller 
{	private static Logger log = LoggerFactory.getLogger(PromocoesCotroller.class);
	@Autowired private PromocaoRepository proRepository;
	@Autowired private CategoriaRepository catRepository;
	
	//---------------------------------------------------------------------------------------------
	//1-Primeiro método responsável por abrir a página
	@GetMapping("/tabela")
	public String showTable()	{return "promo-datatables";}
	
	//2-Segundo método responsavel por receber e responder as requisições. O parâmetro serve para que
	//tenhamos acesso às variáveis que estão na solicitação. A url dessa request esta sem o /promocao
	//pois ja esta incluso no RequestMapping da classe.
	@GetMapping("/datatables/server")
	public ResponseEntity<?> dataTables(HttpServletRequest request)
	{	Map<String, Object> data = new PromocaoDataTablesService().execute(proRepository, request);
		return ResponseEntity.ok(data);
	}
	
	//metodo publico de deletar promocao
	@GetMapping("/delete/{id}")
	public ResponseEntity<?> deletePromocao(@PathVariable("id") Long id)
	{	this.proRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	//metodo para enviar os dados da promocao selecionada para edição de dados de promocao
	@GetMapping("/edit/{id}")
	public ResponseEntity<?> getEditPromocao(@PathVariable("id") Long id)
	{	//o metodo get() é necessário, pois o findById(id) retorna um Optional<Promocao>
		Promocao promocao = this.proRepository.findById(id).get();
		return ResponseEntity.ok(promocao);
	}
	
	//metodo para fazer o update
	@PostMapping("/edit")
	public ResponseEntity<?> updatePromocao(@Valid PromocaoDTO dto, BindingResult result)
	{	if(result.hasErrors())
		{	Map<String, String> errors = new HashMap<>();
			//looping por todos os erros de validação
			for(FieldError error:result.getFieldErrors())
			{	//cria se uma mapeamento das mensagens de erro de acordo com o campo em que poderá ocorrer o erro
				errors.put(error.getField(), error.getDefaultMessage());
			}
			
			//um erro do tipo 422
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		//para trabalhar com operação de update, é necessário de um objeto promocao que esteja em estado persistente
		Promocao promocao = this.proRepository.findById(dto.getId()).get();
		//iremos agora recuperar os dados do formulário de edição de promocao
		promocao.setCategoria(dto.getCategoria());
		promocao.setDescricao(dto.getDescricao());
		promocao.setLinkImagem(dto.getLinkImagem());
		promocao.setPreco(dto.getPreco());
		promocao.setTitulo(dto.getTitulo());
		
		//fazendo a alteração dos dados
		this.proRepository.save(promocao);
		
		return ResponseEntity.ok().build();
	}
	
	//---------------------------AUTOCOMPLETE------------------------------------------------------
	@GetMapping("/site")//este termo é o termo criado no ajax do autocomplete (promo-list.js)
	public ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo)
	{	List<String> sites = this.proRepository.findSitesByTermo(termo);
		return ResponseEntity.ok(sites);
	}
	
	@GetMapping("/site/list")
	public String listarPorSite(@RequestParam("site") String site, ModelMap model)
	{	//trabalhando com ordenação dos elementos, de forma descendente, por data
		Sort sort = new Sort(Sort.Direction.DESC, "cadastro");
		/*Classe que ficará responsável pela limitação de quantidade de conteúdos
		 a serem exibidos dentro das listas de promoções. Neste caso a página inicial
		 será aquele de valor zero*/
		PageRequest pr = PageRequest.of(0, 8, sort);
		model.addAttribute("promocoes", this.proRepository.findBySite(site, pr));
		return "promo-card";
	}
	
	//---------------------------ADD LIKES------------------------------------------------------
	//"?" é um valor genérico de ResponseEntity.
	//"/like" é o URI como caminho de acesso ao repositório para o LIKE
	//"{id}" é o parâmetro que receberá o valor do id da promoção
	@PostMapping("/like/{id}")
	public ResponseEntity<?> addLikes(@PathVariable("id") Long id)
	{	this.proRepository.updateSomarLikes(id);
		//int likes = this.proRepository.findLikesById(id);
		return ResponseEntity.ok(this.proRepository.findLikesById(id));
	}
	
	//---------------------------Listar Ofertas-------------------------------------------------
	//método que retorna a página de listagem de ofertas
	@GetMapping("/list")
	public String listarOfertas(ModelMap model)
	{	//trabalhando com ordenação dos elementos, de forma descendente, por data
		Sort sort = new Sort(Sort.Direction.DESC, "cadastro");
		/*Classe que ficará responsável pela limitação de quantidade de conteúdos
		 a serem exibidos dentro das listas de promoções. Neste caso será apenas (n-1) numero de páginas
		 e 8 promoções, em órdem de criação*/
		PageRequest pr = PageRequest.of(0, 8, sort);
		model.addAttribute("promocoes", this.proRepository.findAll(pr));
		return "promo-list";
	}
	
	@GetMapping("/list/ajax")//o default value serve para setarmos um valor padrão do parâmetro, quando o mesmo não é enviado, e seu tipo será um inteiro
	public String listarCards(@RequestParam(name="page", defaultValue = "1") int page, 
							  @RequestParam(name="site", defaultValue = "") String site, ModelMap model)
	{	//trabalhando com ordenação dos elementos, de forma descendente, por data.
		//esta consulta não se importa com o nome do sitee uma consulta que vai precisar 
		//ser realizada a partir do nome do site.  
		Sort sort = new Sort(Sort.Direction.DESC, "cadastro");
		/*Classe que ficará responsável pela limitação de quantidade de conteúdos
		 a serem exibidos dentro das listas de promoções. Neste caso será apenas (n-1) numero de páginas
		 e 8 promoções, em órdem de criação*/
		PageRequest pr = PageRequest.of(page, 8, sort);
		//a partir da variavel site, iremos saber que declaramos a partir da assinatura do método.
		//Quando esta variável estiver vazia signfica que a gente vai utilizar a consulta findAll().
		//quando a variavel não estiver vazia então significa que a consulta vai ser realizada pelo
		//nome do site.
		if(site.isEmpty())	{model.addAttribute("promocoes", this.proRepository.findAll(pr));}
		
		else model.addAttribute("promocoes", this.proRepository.findBySite(site, pr));
		
		return "promo-card";
	}
	//---------------------------Add Ofertas----------------------------------------------------
	
	//método publico de salvar que retorna um objeto da classe Promocao.
	//quando for enviado uma requisição na URI /promocao/save, será feita
	//neste método o salvar.
	@PostMapping("/save")//o pto de interrogação permite que o retorno seja de um valor genérico
	public ResponseEntity<?> salvar(@Valid Promocao promocao, 
		//este BindResult vai poder nos dar tomadas de decisão quando houver ou 
		//não um problema com a validação, ou seja, se um campo pelo menos não passou
		//nas regras de validação, poderemos tomar a decisão de não responder requisição
		//com sucesso, mas sim enviar para o client side, uma resposta dizendo que um dos campos
		//não passou nas regras de validação
		BindingResult result) 
	{	//sempre usamos um objeto de BindResult em uma condição
		if(result.hasErrors())
		{	Map<String, String> errors = new HashMap<>();
			//looping por todos os erros de validação
			for(FieldError error:result.getFieldErrors())
			{	//cria se uma mapeamento das mensagens de erro de acordo com o campo em que poderá ocorrer o erro
				errors.put(error.getField(), error.getDefaultMessage());
			}
			//um erro do tipo 422
			return ResponseEntity.unprocessableEntity().body(errors);
		}
		log.info("Promocao {}", promocao.toString());
		//salva também a data de cadastro
		promocao.setCadastro(LocalDateTime.now());
		//método de persistencia
		this.proRepository.save(promocao);
		//não teremos nenhum retorno a num ser uma mensagem de sucesso, que o proprio
		//método OK envia pelo HTTP.
		return ResponseEntity.ok().build();
	}
	
	//lista de categoria que se situa na página promo-add.html em
	//uma tag select. o atributo "categorias" é o nome da variável que irá conter a lista e que
	//irá levar a lista para a página. Além disso, este faz referência às categorias da página
	//<option th:each="c : ${categorias}" th:text="${c.titulo}" th:value="${c.id}">...</option>
	@ModelAttribute("categorias")
	public List<Categoria> getCategorias()	{return this.catRepository.findAll();}
	
	/*método responsável por abrir a página de promoções. Quando a
 	controller receber a requisição /promocao/add, cairá no método
 	abrir cadastro e abre a página promo-add.html*/
	@GetMapping("/add")
	public String abrirCadastro()	{return "promo-add";}
}