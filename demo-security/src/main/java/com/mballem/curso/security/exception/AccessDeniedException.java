package com.mballem.curso.security.exception;
/*RuntimeException Exceção padrão do java usado para que uma
 *exceção seja lançada em tempo de execução, bem como o nome já diz*/
@SuppressWarnings("serial")
public class AccessDeniedException extends RuntimeException
{	public AccessDeniedException(String msg) 
	{super(msg);}
}