//inicilizando a página inicial
var pageNumber = 0;

// função de rotina que irá executar toda vez que
// abrirmos a página pela primeira vez. O método
// ready funciona para quando abrir a página, tudo que
// tiver dentro dele seja executado logo no início
/*
 * $(document).ready(function() { //tanto o botão quanto o loader irá sumir
 * //$("#loader-img").hide(); $("#fim-btn").hide(); });
 */

// efeito infinite scroll
// recurso que temos no JQuery para dizer que este analise o tamanho
// da tela, para quando estamos usando a barra de rolagem ou o scroll
// do mouse
$(window).scroll(function() { // este "this" do parâmetro é referente ao
	// "window" declarado acima e
	// que queremos que pegue a altura da barra de rolagem desde de cima
	var scrollTop = $(this).scrollTop();

	// este "document" irá nos trazer o valor da altura referente apenas a
	// página ocupada com o rolar do scroll da página
	var conteudo = $(document).height() - $(window).height();

	// console.log('scrollTop: ', scrollTop, ' | ', 'conteudo', conteudo);

	if (scrollTop >= conteudo) { // console.log("***");
		pageNumber++;
		// chamada de uma função do JS, que funciona como uma espécie de delay
		setTimeout(function() { // chamada da função ajax definido abaixo
			loadByScrollBar(pageNumber);
			// definindo um tempo de time out de 200 ms
		}, 200);
	}
});

// função que irá atualizar a página
// conforme o numero inserido como parâmetro
function loadByScrollBar(pageNumber) { // assim quando a paginação for
	// executado, vamos até o campo de input
	// e capturamos então
	// o valor que tiver la. Mas não basta apenas capturar o valor que temos la
	// no campo de
	// input, temos que pegar esse valor e incluir numa variável e enviar para o
	// controller
	// pela requisição.
	var site = $("#autocomplete-input").val();
	$.ajax({
		method : "GET",
		url : "/promocao/list/ajax",
		// variável page a ser inserido na controller, com requisição
		// tipo GET para carregamento da página.
		// No parâmetro Data, abaixo da variável page
		data : {
			page : pageNumber,
			// aqui será o site, onde fica o campo de input para escrever o site
			// de busca
			site : site
		},
		beforeSend : function() {
			$("#loader-img").show();
		},

		// o parâmetro response será a variável
		// retornado após o sucesso da requisição
		success : function(response) { // console.log("resposta: ", response);
			console.log("lista > ", response.length);
			if (response.length > 150) { // método fadeIn para que os métodos
				// sejam feitas de forma gradual,
				// com 250ms
				$(".row").fadeIn(250, function() { // o componente this faz
					// referencia ao componente
					// da classe row
					$(this).append(response);
				});
			} else {
				$("#fim-btn").show();
				// remove o iframe loader
				$("#loader-img").removeClass("loader");
			}
		},

		error : function(xhr) {
			alert("Oops, ocorreu um erro: " + xhr.status + " - "
					+ xhr.statusText);
		},

		complete : function() {
			$("#loader-img").hide();
		}
	});
}

// autocomplete, é um método do JQuery que obtemos a partir do JS que
// adicionamos na aplicação
// da aula anterior.
$("#autocomplete-input").autocomplete({ // este atributo do JQuery deve ser
	// sempre inicializado com uma function.
	// os atributos da function vão servir para que a gente envie a solicitação
	// e deopois receba a resposta da solicitação.
	source : function(request, response) {
		$.ajax({
			method : "GET",
			url : "/promocao/site",
			// essa forma que o JQuery pega o valor do campo e ja atribui o
			// valor capturado nessa
			// variavel criada "termo", que será envaida para a controller
			data : {
				termo : request.term
			},
			success : function(result) {
				response(result);
			}
		});
	}
});

$("#autocomplete-submit").on(
	"click",
	function() { // recuperar o nome do site que temos no campo de input
		var site = $("#autocomplete-input").val();
		$.ajax({
			method : "GET",
			url : "/promocao/site/list",
			// este parâmetro irá levar o nome do site para o lado do
			// servidor
			data : {
				site : site
			},
			beforeSend : function() { // incializando a página como 0,
				// pois suponha que lá na página
				// a gente já tenha feito duas ou três paginações, então
				// pageNumber
				// vai estar valendo 2, 3, etc. A gente precisa a partir
				// desta nova
				// requisição zerar o número da página pois a lista de
				// promoções que
				// vamos buscar a partir de um site específico deve vir na
				// posição 0,
				// pois também vamos trabalhar com paginação desta consulta.
				pageNumber = 0;
				// esconde o botão
				$("#fim-btn").hide();
				// sumir com os cards das promoções
				$(".row").fadeOut(400, function() { // este método vai
					// limpar tudo que tem
					// dentro do <div
					// class="row">, o que
					// fará com que os cards desapareçam da página
					$(this).empty();
				});
			},
			// o parâmetro response vai ser o objeto de retorno do servidor
			success : function(response) { // cria os novos cards na página
				$(".row").fadeIn(250, function() { // este parâmetro será o
					// responsável por
					// incluir os cards lá
					// na página
					$(this).append(response);
				});
			},
			error : function(xhr) {
				alert("Ops, algo deu errado: " + xhr.status + ", "
						+ xhr.statusText);
			}
		});
	});

// add likes. Como existem vários ids com este mesmo nome inicial, será feito um
// vetor, cujo
// valor indexado é o id do mesmo. O * vai dizer para o JQuery que deve esperar
// por um click
// em qualquer botão que tenha instrução likes-btn-. o método on() é onde
// indicamos o tipo de
// método a ser usado, que no caso é o click
// $("button[id*='likes-btn-']").on("click", function()

/*
 * Muitos botões de likes não funcionarão pois muitos deles não são apresentados
 * na abertura da página, pois inicialmente não fazem parte do DOM. Teremos que
 * dar um jeito de incluí-los. Através deste "document", estamos dizendo para o
 * JQuery para acessar o objeto document que contém os componentes da página.
 * Assim iremos dizer então para ele o que a gente quer que procure, através do
 * parâmetro do meio da função incluída
 */
$(document).on(
		"click",
		"button[id*='likes-btn-']",
		function() { // o método split() fará a separação do id por hifem, e
			// tudo que não for hifem, irá colocar
			// em uma posição de um array. O funcionamento deste é semelhante a
			// do java. O colchetes
			// indica a posição do botão.
			var id = $(this).attr("id").split("-")[2];
			// para verificar se estamos recuperando o valor do id do botão que
			// está sendo clicado
			console.log("id: ", id);

			// add a função AJAX para requisição do like
			$.ajax({
				method : "POST",
				url : "/promocao/like/" + id,
				// resposta do lado do servidor, como parâmetro
				success : function(response) {
					$("#likes-count-" + id).text(response);
				},

				error : function(xhr) {
					alert("Oops, ocorreu um erro: " + xhr.status + ", "
							+ xhr.statusText);
				}
			});
		});

// AJAX REVERSE
// essa variável estará recebendo o valor que está vindo pelo count, quando
// o servidor envia a mensagem como total de novas promoções que tem para
// serem exibidas. Por exemplo, recebemos 2 promoções a variável total ofertas
// recebe então o valor 2. Porém, caso o usuário ainda não tenha clicado no
// botão e o servidor envia mais uma contagem, então terá que incrementar a variável.
var totalOfertas = 0;

//chamada da função init() quando for aberta a página
$(document).ready(function() {init();});

function init() 
{	console.log("dwr started...");
	
	//devemos chamar a servlet, cuja URL foi definida: localhost:8080/dwr 
	// habilitar o uso so ajax reverso no lado do cliente
	dwr.engine.setActiveReverseAjax(true);

	// captura mensagens de erro
	dwr.engine.setErrorHandler(error);

	// declarando a classe que teremos no lado servidor que vai trabalhar com
	// AJAX reverso
	DWRAlertaPromocoes.init();
}

// função que veririfa se há erro
function error(exception) {console.log("dwr error: ", exception);}

// função responsável por receber as informações que o servidor esta enviando
// aqui para o cliente quando recebermos do servidor a informação que vai ser o total de promoções
// que foram cadastradas
// no sistema, nós vamos ter que exibir o total para o usuário.
function showButton(count) 
{	totalOfertas = totalOfertas + count;
	$("#btn-alert").show(function() 
	{	$(this).attr("style", "display:block;").text(
			"Veja " + totalOfertas + " nova(s) oferta(s)!");
	});
}

$("#btn-alert").on("click", function() 
{ // recuperar o nome do site que temos no campo de input
	$.ajax(
	{	method : "GET",
		url : "/promocao/list/ajax",
		// este parâmetro irá levar o nome do site para o lado do
		// servidor
		data : {
			page : 0//ultimas 8 promoções, a partir do ultimo
		},
		beforeSend : function() { // incializando a página como 0,
			// pois suponha que lá na página
			// a gente já tenha feito duas ou três paginações, então
			// pageNumber
			// vai estar valendo 2, 3, etc. A gente precisa a partir
			// desta nova
			// requisição zerar o número da página pois a lista de
			// promoções que
			// vamos buscar a partir de um site específico deve vir na
			// posição 0,
			// pois também vamos trabalhar com paginação desta consulta.
			pageNumber = 0;
			totalOfertas = 0;
			// esconde o botão
			$("#fim-btn").hide();
			
			//essa classe que irá fazer surgir a classe de loader
			$("#loader-img").addClass("loader");
			
			$("#btn-alert").attr("style", "display:none;");
			
			// sumir com os cards das promoções
			$(".row").fadeOut(400, function() { // este método vai
				// limpar tudo que tem
				// dentro do <div
				// class="row">, o que
				// fará com que os cards desapareçam da página
				$(this).empty();
			});
		},
		// o parâmetro response vai ser o objeto de retorno do servidor
		success : function(response) { // cria os novos cards na página
			$("#loader-img").removeClass("loader");
			$(".row").fadeIn(250, function() { // este parâmetro será o
				// responsável por
				// incluir os cards lá
				// na página
				$(this).append(response);
			});
		},
		error : function(xhr) {
			alert("Ops, algo deu errado: " + xhr.status + ", "
					+ xhr.statusText);
		}
	});
});