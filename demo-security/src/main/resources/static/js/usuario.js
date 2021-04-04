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
						.concat('id="dp_').concat(id).concat('"', ' ') 
						.concat(
						'role="button" title="Editar" data-toggle="tooltip" data-placement="right">', ' ')
						.concat('<i class="fas fa-edit"></i></a>');
					},
					orderable : false
				}
		]
	});
});	