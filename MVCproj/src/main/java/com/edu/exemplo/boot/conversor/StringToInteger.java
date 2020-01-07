package com.edu.exemplo.boot.conversor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component//notação necessária para validação
public class StringToInteger implements Converter<String, Integer>
{	@Override public Integer convert(String source) 
	{	source = source.trim(); //método da string que ignora preenchimento de espaços em branco antes e depois de um texto
		//o sinal de "+" significa que pode aparecer mais dígitos entre 0 e 9, como valor da variável text. Esta condição
		//irá verificar se o proximo digito digitado por último é do tipo numérico entre 0 a 9
		if(source.matches("[0-9]+"))
		{return Integer.parseInt(source);}
		
		//se o valor digitado não for numérico irá retornar um valor nulo e isso irá
		//para o método de validação da notação @NotNull do atributo "numero" da classe
		//Endereço
		return null;
	}	
}