//JQuery para submit do formulário para o controller. O método submit vai
//simular o botão de submit que teríamos em uma página HTML, que fosse fazer
//uma requisição normal, ou seja sem uso de AJAX. O parâmetro "e" é um apelido
//para qualquer evento, que será usado pois iremos trabalhar com o método que
//inibe o comportamento padrão do submit 
$("#form-add-promo").submit(function(e)
{	//este método de evento evita que o evento padrão do submit seja executado.
	//pois quando temos um submit, o navegador por padrão vai fazer uma requisição
	//ao lado do servidor da aplicação, e essa requisição vai gerar também um refresh
	//no navegador. Se não colocarmos este método, será gerado um refresh assim que
	//a requisição de submit for realizado.
	e.preventDefault();
	
	//variável que irá representar o objeto de promoção, que será inicializada com
	//duas chaves. Essa inicialização fará com que o JS crie então um objeto do tipo
	//promo.
	var promocao = {};
	//criando um atributo do objeto promocao
	promocao.linkPromocao = $("#linkPromocao").val();
	promocao.descricao = $("#descricao").val();
	promocao.preco = $("#preco").val();
	promocao.titulo = $("#titulo").val();
	promocao.categoria = $("#categoria").val();
	promocao.linkImagem = $("#linkImagem").attr("src");
	promocao.site = $("#site").text();
	
	//add um console.log
	console.log("promo > ", promocao);
	
	$.ajax({
		method: "POST",
		url: "/promocao/save",//URL que deve ser criada na controller
		data: promocao,
		beforeSend: function()//método que esconde algo que contém na página antes de 
		{	//removendo as mensagens, o parâmetro error-span é a classe que tem declarada no span,
			//e o metodo remove() irá remover tudo que contém na tag que contém a classe error-span
			$("span").closest('.error-span').remove();
			//removendo as bordas vermelhas nas bordas dos campos
			$("#caterogia").removeClass("is-invalid");
			$("#preco").removeClass("is-invalid");
			$("#linkPromocao").removeClass("is-invalid");
			$("#titulo").removeClass("is-invalid");
			
			//habilita o load
			$("#form-add-promo").hide();//executar a requisição.
			//o método show() é o oposto ao hide
			$("#loader-form").addClass("loader").show();
		},
		success: function()	//método each() para fazer a limpeza dos campos do form após realizar o cadastro, como se fosse um objeto de tipo formulário
		 {	$("#form-add-promo").each(function()	{this.reset();});
		 	$("#linkImagem").attr("src", "/images/promo-dark.png");
		 	$("#site").text("");
		 	//primeiramente, mesmo que não tenha o alerta de erro, vai ser feita tentativa de retirada desta
			$("#alert").removeClass("alert alert-danger").addClass("alert alert-success").text("Promoçao cadastrada!");
		 },
		statusCode:		
		{	//códigos dos retornos de validação	
			422: function(xhr)//parâmetro importante para pegar o valor das msgs de erro
			 {	console.log('status error:', xhr.status);//este último parâmetro vai servir para mostrar o código de erro do console
				var errors = $.parseJSON(xhr.responseText);
				//método each em JQuery para um looping de uma lista
				//o parâmetro key é a chave do map e val é o valor
				$.each(errors, function(key, val)
				{	//usando recursos do JQuery para achar os ID dos campos
					//a classe do bootstrap chamada is-invalid responsavel por colocar
					//a borda vermelha no campo que tiver um valor inválido ou vazio
					$("#" + key).addClass("is-invalid");
					//inclui a classe que faz com que a mensagem de erro tenha cor vermelha.
					//o método append é que insere a mensagem
					$("#error-" + key).addClass("invalid-feedback").append("<span class='error-span'>" +
						val +"</span>");
				});
			 }
		},
		error: function(xhr)//o parâmetro xhr nos trará uma mensagem referente ao erro que possa ocorrer durante a requisição.	
		   {	//o responseText será o método que vai nos exibir a mensagem referente ao erro. 
				console.log("> error: ", xhr.responseText);//mais informações, visite a página de JQuery
				$("#alert").addClass("alert alert-danger").text("Não foi possível realizar o cadastro");
		   },
	   complete: function()//método semelhante ao hide, porém esconde o que ja tem na página de forma gradual, como uma transição suave 
		  {		$("#loader-form").fadeOut(800, function()
				{	$("#form-add-promo").fadeIn(250);//traz de volta tudo que o fadeOut escondeu	
			  		$("#loader-form").removeClass("loader");
				});
		  }		
	});
});

//função para capturar as metatags.
//essa instrução indica que queremos acessar o componente da página HTML que possui o ID linkPromocao
$('#linkPromocao').on('change', function() 
{	var url = $(this).val();//recurso do JQuery para recuperar o valor do campo de input com id="linkPromocao"
	//pois toda URL vai ter pelo menos o http:// (ou seja, 7 caracteres)
	if(url.length > 7)
	{	$.ajax(//lembrando que inserimos na controller uma requisição do tipo POST
		{	method:"POST",
			url:"/meta/info?url=" + url,
			cache:false,//este recurso impede que os dados sejam armazenados em cache
			beforeSend://aqui será realizado a limpeza dos campos de entrada de dados antes de enviar a requisição
				function()//abaixo, remove a mensagem de erro
				{	$("#alert").removeClass("alert alert-danger alert-success").text("");
					//deixo o campo de titulo, site e imagem vazias
					$("#titulo").val("");
					$("#site").text("");
					$("#linkImagem").attr("src", "");
					//inserção do loading
					$("#loader-img").addClass("loader");
				},
			success:
				function(data)//data é um parâmetro que recebem o resultado da operação
				{	console.log(data);//isso será enviado os resultado da operação no log do navegador	
					//no campo em que o id="titulo" será inserido o resultado da requisição "data"
					//com a informação titulo
					$("#titulo").val(data.title);
					//campo que não é <input>, por isso será usado o método text()
					//o metodo replace serviu para retirar o "@" abaixo da imagem
					$("#site").text(data.site.replace("@", ""));
					//o parâmetro src que será o atributo que iremos acessar
					$("#linkImagem").attr("src", data.image);
				},
			statusCode: 
			{	404: function()//método do bootstrap 4 que insere uma classe que deixa o alert em vermelho
				 {	$("#alert").addClass("alert alert-danger").text(
						 "Nenhuma informação pode ser recuperada dessa URL.");
				 	//a imagem padrão somente irá aparecer após o carregamento completo e caso aconteça um erro
				 	$("#linkImagem").attr("src", "/images/promo-dark.png");
				 }
			},
			error: function()
			   {	$("#alert").addClass("alert alert-danger").text(
					   "Ops... algo deu errado, tente mais tarde");
			   },
			complete: function()
			  {	//assim que a imagem aparecer, remove o loading
				$("#loader-img").removeClass("loader");
			  }
		});
	}
});