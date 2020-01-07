package com.edu.exemplo.boot.conversor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.edu.exemplo.boot.domain.Departamento;
import com.edu.exemplo.boot.service.DepartamentoService;
//classe que ficará responsável pela conversão de uma string da requisição
//na entidade Departamento
@Component
public class StringToDeptoConv implements Converter<String, Departamento>
{	@Autowired private DepartamentoService service;
	
	@Override public Departamento convert(String source) 
	{	if(source.isEmpty())	{return null;}
		
		//convertento um tipo String para um tipo Long
		Long id = Long.valueOf(source);
		
		//com a consulta do departamento, será possível encontrar o depto deste
		//ID selecionado
		return service.buscarPorId(id);
	}		
}