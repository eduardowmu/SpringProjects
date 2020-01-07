package com.edu.exemplo.boot.validator;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.edu.exemplo.boot.domain.Funcionario;

/*classe de validação usando spring validator, que é uma técnica de validação
 * que permite criar regras mais avançadas para validação*/
public class FuncionarioValidator implements Validator
{	/*este método tem como princípio validar o objeto que estaremos enviando a partir do formulário
 	com aquele objeto que realmente esta classe deve validar*/
	@Override public boolean supports(Class<?> clazz) 
	{return Funcionario.class.equals(clazz);}

	/*O object é o objeto que estamos recebendo do formulário*/
	@Override public void validate(Object object, Errors errors) 
	{	Funcionario funcionario = (Funcionario)object;
		//implementação da validação de datas entrada/saída
		LocalDate entrada = funcionario.getDataEntrada();
		LocalDate saida = funcionario.getDataSaida();
		
		if(saida != null)
		{	if(saida.isBefore(entrada))
			{errors.rejectValue("dataSaida", "PosteriorDataEntrada.funcionario.dataSaida");}
		}
	}
}