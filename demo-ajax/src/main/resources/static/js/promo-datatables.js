//javascript necessário que iremos desenvolver para trabalhar com datatables. datatables.net
//a instrução fará com que o código que temos dentro da função ready seja executado
//pelo JQuery logo depois que a página HTML for aberta. Quando a gente abrir a página,
//a gente já quer que a tabela apareça na página, e não esperar que aconteça com um clique
$(document).ready(function()
{	//formatação de data no formato pt-br
	moment.locale('pt-br');
	//perceba que em promo-datatables.html, temos o id="table-server"
	//que indica qual tabela irá aparecer. A essa nova variável table
	//será lhe atribuído a tabela dataTable.
	var table = $("#table-server").DataTable
	({	//parâmetro que vai fazer que quando estiver por exemplo paginando sua tabela
		//apareça uma barrinha no meio da tabela com a instrução processing, que é como
		//se fosse um loading, para mostrar ao usuário que os dados estão sendo carregado.
		processing: true,
		
		//este parâmetro habilita a datatables para que trabalhemos no lado servidor
		//com ela
		serverSide: true,
		
		//como se fosse uma classe CSS que faz com a tabela tenha um comportamento reponsivo
		responsive: true,
		
		//tamanho do menu de páginas
		lengthMenu: [10, 15, 20, 25],
		
		ajax:
		{	url: "/promocao/datatables/server",
			
			//paraêmtroe que recebe os dados, que representa nossa objeto que está sendo retornado
			//para o cliente pelo servidor 
			data: "data",
		},
		columns: 
		[	{data: 'id'},
			{data: 'titulo'},
			{data: 'site'},
			{data: 'linkPromocao'},
			{data: 'descricao'},
			{data: 'linkImagem'},
			//com tratamento de características monetárias. instrução própria da dataTable
			//para formatarmos o valor que será exibido na página. Fará a substituição do
			//ponto para vírgula e formatará para limitar a duas casas decimais e inserir o
			//símbolo do real.
			{data: 'preco', render: $.fn.dataTable.render.number('.', ',', 2, 'R$')},
			{data: 'likes'},
			//referencia: moment.js.com OU cdnjs.com (moment.js)
			{	data: 'cadastro', render: function(dtCadastro)
				{	//parametro 'LLL' foi tirado da página momentjs.com
					return moment(dtCadastro).format('LLL');	
				}
			},
			{data: 'categoria.titulo'}
		],
		//inclusão de um parâmetro JS DOM, que a dataTables usa para informar
		//ou para levar ao objeto DOM da página que ela tem 2 botões que precisam
		//fazer parte daquele objeto.
		dom: 'Bfrtip',
		//inclusão dos botões de delete e edit
		buttons: //estes textos irão aparecer nos botões sendo inclusos
		[	{	text: 'Editar',
				//para informar o tipo do elemento
				attr: {
					id: 'btn-editar',
					type: 'button'
				},
				//inclusão de um parametro que indica que os botões
				//estão inabilitados. Mas para o JQuery, isso não importa.
				enabled: false
			},
			{	text: 'Excluir',
				attr: {
					id: 'btn-excluir',
					type: 'button'
				},
				enabled: false
			}
		]
	});
	
	//ação para marcar e desmarcar botões ao clicar na ordenação 
	$('#table-server thead').on('click', 'tr', function()
	{table.buttons().disable();});
	
	//recusro JS que faz uma marcação na linha da tabela ao clicar na mesma.
	//o parâmetro tbody, repare que em promo-datatables.html temos o <thead></thead>
	//que é para o cabeçalho. Dentro da tabela, a parte referente as linhas ficarão
	//dentro das tags tbody, onde estarão as linhas <tr></tr>.
	$('#table-server tbody').on('click', 'tr', function()
	{	//se a linha clicada já não estiver selecionada. O método hasClass() é do JQuery
		//e funciona como um método que te retorna um booleano
		if($(this).hasClass('selected'))	
		{	$(this).removeClass('selected');
			
			//em JS temos como identificar o botão
			table.buttons().disable();
		}
		
		else 
		{	//quando um usuário selecionar uma linha e uma outra já estiver selecionada,
			//devemos remover a seleção desta linha e que a nova linha selecionada seja a
			//unica selecionada. O JQuery vai fazer uma varredura para verificar se existe
			//alguma linha selecionada. Se não tiver, não surtirá efeito nenhum.
			$('tr.selected').removeClass('selected');
			//seleção da nova linha.
			$(this).addClass('selected');
			table.buttons().enable();
		}
	});
	
	//instrução JQuery para acessar os botões pelo ID
	$("#btn-editar").on('click', function()
	{	if(isSelectedRow())
		{	//pega o valor do id da linha de promocoes
			var id = getPromocaoId();
			
			//requisição ajax para busca das informações da promocao
			//conforme através do seu id
			$.ajax(
			{	method: "GET",	
				url: "/promocao/edit/" + id,
				beforeSend: function()
				{	//removendo as mensagens, o parâmetro error-span é a classe que tem declarada no span,
					//e o metodo remove() irá remover tudo que contém na tag que contém a classe error-span
					$("span").closest('.error-span').remove();
					//removendo as bordas vermelhas nas bordas dos campos
					$(".is-invalid").removeClass("is-invalid");
					$("#modal-form").modal('show');
				},
				//o parametro da função é que nos trará os dados
				//da promoção buscada
				success: function(data)
				{	$("#edt_id").val(data.id);
					$("#edt_site").text(data.site);
					$("#edt_titulo").val(data.titulo);
					$("#edt_descricao").val(data.descricao);
					//método do JQuery para formatar o valor em padrão R$
					$("#edt_preco").val(data.preco.toLocaleString('pt-BR', 
					{minimumFractionDigits: 2, maximumFractionDigits: 2}));//limitando as frações decimais em duas casas
					//se formos na promo-datatables-modal.html, vemos que o campo recebe o parametro através do seu ID
					$("#edt_categoria").val(data.categoria.id);
					$("#edt_linkImagem").val(data.linkImagem);
					//o campo de imagem não é um <input/> e nem <text>
					$("#edt_imagem").attr('src', data.linkImagem);
				},
				error: function()
				{alert('Estamos com problemas no serviço do Banco de dados');}
			});
			
			//$('#modal-form').modal('show');
			//acesso a um componente da variavel table
			//var id = getPromocaoId();
		}
	});
	
	//submit da modal de formulario de update
	$("#btn-edit-modal").on('click', function()
	{	var promocao = {};
		//criando um atributo do objeto promocao
		promocao.descricao = $("#edt_descricao").val();
		promocao.preco = $("#edt_preco").val();
		promocao.titulo = $("#edt_titulo").val();
		promocao.categoria = $("#edt_categoria").val();
		promocao.linkImagem = $("#edt_linkImagem").val();
		promocao.id = $("#edt_id").val();
		
		$.ajax(
		{	method: "POST",
			url: "/promocao/edit",
			data: promocao,
			beforeSend: function()//método que esconde algo que contém na página antes de 
			{	//removendo as mensagens, o parâmetro error-span é a classe que tem declarada no span,
				//e o metodo remove() irá remover tudo que contém na tag que contém a classe error-span
				$("span").closest('.error-span').remove();
				//removendo as bordas vermelhas nas bordas dos campos
				$(".is-invalid").removeClass("is-invalid");
			},
			success: function()
			{	$("#modal-form").modal("hide");
				table.ajax.reload();
			},
			//status 422, quando o formulário nao passa na validação
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
						$("#edt_" + key).addClass("is-invalid");
						//inclui a classe que faz com que a mensagem de erro tenha cor vermelha.
						//o método append é que insere a mensagem
						$("#error-" + key).addClass("invalid-feedback").append("<span class='error-span'>" +
							val +"</span>");
					});
				 }
			}
		});
	});
	
	//alterar a imagem no componente <img> do modal
	$("#edt_linkImagem").on("change", function()
	{	//recupera do campo de link da imagem
		var link = $(this).val();
		$("#edt_imagem").attr("src", link);
	});
	
	$("#btn-excluir").on('click', function()
	{	if(isSelectedRow())
		{	//pegando o id da modal, método bootstrap
			//para que abra uma modal
			$('#modal-delete').modal('show');
			//acesso a um componente da variavel table
			//var id = getPromocaoId();
		}		
	});
	
	//metodo de requisição da exclusão
	$('#btn-del-modal').on('click', function()
	{	var id = getPromocaoId();
		$.ajax(
		{	method: "GET",
			url: "/promocao/delete/" + id,
			success: function()
			{	//a modal de delete se esconde
				$("#modal-delete").modal('hide');
				//quando excluirmos uma linha iremos excluir a linha
				//lá do BD. Fazendo assim o carregamento completo da tabela
				table.ajax.reload();  
			},
			error: function()
			{alert('Estamos com problemas no serviço do banco de dados.');}
		});
	});
	
	function getPromocaoId()
	{return table.row(table.$('tr.selected')).data().id;}
	
	function isSelectedRow()
	{	//para verificar se a linha esta selecionada ou não
		var trow = table.row(table.$('tr.selected'));
		return trow.data() !== undefined;
	}
});