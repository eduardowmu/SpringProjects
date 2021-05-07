//datatables - lista de médicos
$(document).ready(function() {
	moment.locale('pt-BR');
	var table = $('#table-usuarios').DataTable({
		searching : true,
		lengthMenu : [ 5, 10 ],
		processing : true,
		serverSide : true,
		responsive : true,
		ajax : {/*URL a ser configurado na controller da lista
		 		de usuários para o datatable*/
			url : '/u/datatables/server/usuarios',
			data : 'data'
		},
		columns : [
				{data : 'id'},
				{data : 'email'},
				{	/*Esta coluna no banco de dados possui valor
				 	0 e 1. Mas quando o resultado da coluna chega
				 	na aplicação este 0 e 1 é transformado em 
				 	booleano que será false/true*/
					data : 'ativo', 
					render : function(ativo) {
						return ativo == true ? 'Sim' : 'Não';
					}
				},
				{	/*Na parte de perfis, é um pouco mais complicado
				 	pois temos na classe de usuário uma lista de perfis
				 	então quando recebemos os resultado no JS, iremos
				 	receber um resultado de uma lista. Mas essa lista
				 	é baseada no objeto perfil, que contém o ID e descição
				 	do perfil. O que queremos mostrar na página, é apenas
				 	a descrição do problema do perfil, se é ADMIN, Médico,
				 	ou se é paciente. Por conta disso, usamos a função render
				 	para recuperar essa lista, faremos um "for" nessa
				 	lista e para isso, no jquery temos o método each,
				 	passamos a lista de perfis, para informar sobre qual
				 	lista iremos percorrer com este looping e temos o índice
				 	e um valor. O índice é a posição na lista e o valor seria o
				 	objeto dentro daquela lista. Então nesse caso o objeto é o
				 	objeto perfil da classe perfil.*/
					data : 'perfis',									 
					render : function(perfis) {
						var aux = new Array();
						$.each(perfis, function( index, value ) {
							/*Para que recuperemos a descrição
				 			desse objeto perfil, pegamos a variável 
				 			value.desc, pois na classe perfil, temos
				 			o ID e a descrição. Como queremos a descrição
				 			pegaremos apenas o value.desc. Estamos inserindo
				 			essa descrição em um array. Então criamos um
				 			array auxiliar, com um método push o valor
				 			que estamos recuperando no looping, por exemplo
				 			a descrição de ADMIN, inserindo em um array e o mesmo
				 			para os outros perfis.*/  
							aux.push(value.desc);
						});
						return aux;
					},//não estamos trabalhando com ordenação
					orderable : false,
				},
				{	data : 'id',	
					render : function(id) {
						return ''.concat(
							'<a class="btn btn-success btn-sm btn-block"', ' ')
							.concat('href="').concat(
							'/u/editar/credenciais/usuario/').concat(id, '"', ' ') 
							.concat(
							'role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
							.concat('<i class="fas fa-edit"></i></a>');
					},
					orderable : false
				},
				{	data : 'id',	
					render : function(id) {
						return ''.concat(
							'<a class="btn btn-info btn-sm btn-block"', ' ')
						/*Neste caso não temos o link dentro do botão pois neste precisaremos de
						 *dois parâmetros, o ID e do parâmetro referente aos perfis e não conse-
						 *guimos recuperar dentro de um mesmo elemento o valor de duas colunas
						 *diferentes. Então iremos precisar criar uma função fora do código da tabela
						 *para que possamos acessar as colunas e pegá-los para concatenar a nossa
						 *URL. Para acessarmos os valores que temos na tabela, temos a variável table
						 *ID = table-usuarios, que temos como referente a AJAX que trabalhamos
						 *recebe o objeto DataTable, que é todo conteúdo que existe na tabela que
						 *o JS está criando.*/
						.concat('id="dp_').concat(id).concat('"', ' ') 
						.concat(
						'role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
						.concat('<i class="fas fa-edit"></i></a>');
					},
					orderable : false
				}
		]
	});
	/*Criação do código para com o click em algum dos botões da
	 *coluna dados pessoais. Para que reconhecemos que houve o
	 *click em um daqueles botões vamos trabalhar com JQUERY.
	 *Nosso filtro é qualquer botão que tenha ID que inicie com
	 *dp_*/
	$('#table-usuarios tbody').on('click', '[id*="dp_"]',function()
	{	/*data = nossa lista de colunas*/
		var data = table.row($(this).parents('tr')).data();
		var aux = new Array();
		$.each(data.perfis, function(index, value)
		{aux.push(value.id);});
		document.location.href = '/u/editar/dados/usuario/' +
			data.id + '/perfis/' + aux;
	});
	
});

$('.pass').keyup(function()
{	$('#senha1').val() === $('#senha2').val() ?
		$('#senha3').removeAttr('readonly') :
		$('#senha3').attr('readonly', 'readonly');
});