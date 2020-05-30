package br.edu.fatec2020.topicos.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Getter;

//session é um espaço da memória compartilhado para todas as
//requisições do mesmo navegador. Se fecharmos a janela toda
//do navegador e todas as abas do site aberto, ele perde essa
//referência da session. Se tentar abrir em outro navegador,
//ou janela anônima, será outra session.
/*é possível substituir aquela linha com @Scope(...) apenas por @SessionScope (sem nenhum parâmetro), 
 *ficando muito mais curta a definição*/
//@Scope(scopeName = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS )
@SessionScope()
@Getter
@Component
public class CarrinhoCompra 
{	private List<Produto> produtos = new ArrayList<>();
	
	public void addProduto(Produto produto)	{this.produtos.add(produto);}
	
	public void limparCarrinho() {this.produtos.clear();}
}
