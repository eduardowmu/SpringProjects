package com.edu.exemplo.boot.conversor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Departamento;
import com.edu.exemplo.boot.service.CargoService;

@Component
public class StringToCargoConv implements  Converter<String, Cargo>
{	@Autowired private CargoService service;
	
	//método de conversão do tipo de dado String para outro tipo de dado
	@Override public Cargo convert(String source) 
	{	if(source.isEmpty())	{return null;}
	
		Long id = Long.valueOf(source);
		return this.service.buscarPorId(id);
	}	
}