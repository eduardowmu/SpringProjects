package com.mballem.curso.security.web.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.service.EspecialidadeService;

@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> 
{	/*Injetamos a variável service referente a EspecialidadesService, pois precisaremos
 	fazer uma consulta pelos objetos de especialidades via informações que teremos no BD.
 	No método convert receberemos então como parâmetro o array de strings que será cada
 	especialidade que digitamos no campo de input.*/
	@Autowired private EspecialidadeService service;
	
	/*Vamos criar uma instancia para uma lista de especialidades do tipo SET dentro do 
	 *método*/
	@Override public Set<Especialidade> convert(String[] titulos) 
	{	Set<Especialidade> especialidades = new HashSet<>();
		/* e iremos testar se o array que recebemos é diferente de nulo para evitar
		 *o nullpointer exception e também se o tamanho dele é maior que zero. Assim
		 *temos certeza que esse array, se chegar aqui não irá causar problema de 
		 *nullpointer*/
		if(titulos != null && titulos.length > 0)
		{especialidades.addAll(this.service.buPorTitulos(titulos));}
		return especialidades;
	}
}