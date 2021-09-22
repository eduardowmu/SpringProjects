package br.com.erudio.restwithspringbootudemy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*Para casos não retorne nenhum resultado numa busca 
 *opor exemplo*/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{	private static final long serialVersionUID = 1L;	
	
	public ResourceNotFoundException(String exception)
	{super(exception);}
}