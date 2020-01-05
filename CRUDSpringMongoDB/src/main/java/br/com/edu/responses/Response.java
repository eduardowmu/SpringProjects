package br.com.edu.responses;

import java.util.List;

//classe que pode ser genérica
public class Response<T> 
{	private T data;
	private List<String> errors;
	
	//constructores
	public Response(T data)	{this.data = data;}
	public Response(List<String> errors)	{this.errors = errors;}
	
	public T getData() {return data;}
	public void setData(T data) {this.data = data;}
	
	public List<String> getErrors() {return errors;}
	public void setErrors(List<String> errors) {this.errors = errors;}
}