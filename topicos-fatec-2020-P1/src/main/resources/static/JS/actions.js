$(document).ready(function()
{	confirmouSla = false;
	$(".btn-excluir").on("click", function(e) 
	{	if (!confirmouSla) 
		{	e.preventDefault();
			confirmouSla = confirm("Tem certeza que deseja excluir?");
			if(confirmouSla) {$(this).click();}
		} 
	});
});