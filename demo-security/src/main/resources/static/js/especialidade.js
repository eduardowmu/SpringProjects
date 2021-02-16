$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-especializacao').DataTable({
    	searching: true,
    	order: [[ 1, "asc" ]],//força uma ordenação pela coluna numero 1 da tabela
    	lengthMenu: [5, 10],
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {//requisição ajax para buscar os dados no servidor para preencher a tabela.
            url: '/especialidades/datatables/server',
            data: 'data'
        },//colunas de controle de editar/excluir
        columns: [
            {data: 'id'},
            {data: 'titulo'},
            {orderable: false,//não iremos fazer ordenação 
             data: 'id',
             	//criando um botão que será incluído na coluna. Esse terá URL que através de ID poderesmos
             	//editar/excluir
                "render": function(id) {
                    return '<a class="btn btn-success btn-sm btn-block" href="/especialidades/editar/'+ 
                    	id +'" role="button"><i class="fas fa-edit"></i></a>';
                }
            },
            {orderable: false,
             data: 'id',
                "render": function(id) {
                    return '<a class="btn btn-danger btn-sm btn-block" href="/especialidades/excluir/'+ 
                    	id +'" role="button" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-times-circle"></i></a>';
                }               
            }
        ]
    });
});    
