package br.com.erudio.restwithspringboot.converter;

import java.util.ArrayList;
import java.util.List;
import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
/*Classe de conversão de um objeto para tipo VO*/
public class DozerConverter 
{	/*parametro privado*/
	private static Mapper mapper = DozerBeanMapperBuilder
			.buildDefault();
	
	/*O --> origem
	 *D --> Destino*/
	public static <O, D> D parseObject(O origin, 
			Class<D> destination)
	{return mapper.map(origin, destination);}
	
	public static <O, D> List<D> parseListObjects(List<O> origin, 
			Class<D> destination)
	{	List<D> destinationObjects = new ArrayList<D>();
		/*Quando fizermos um objeto que tem um escopo maior na 
		 *Classe*/
		for (Object o : origin) 
		{destinationObjects.add(mapper.map(o, destination));}
		return destinationObjects;
	}
}