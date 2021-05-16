package com.mballem.curso.security.repository.projecoes;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Paciente;

public interface HistoricoPaciente 
{	Long getId();
 
	Paciente getPaciente();
	
	/*Obtenção de datas em formato de string*/
	String getDataConsulta();

	Medico getMedico();
	
	Especialidade getEspecialidade();
}
