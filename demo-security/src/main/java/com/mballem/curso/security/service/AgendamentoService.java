package com.mballem.curso.security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Horario;
import com.mballem.curso.security.exception.AccessDeniedException;
import com.mballem.curso.security.repository.AgendamentoRepository;
import com.mballem.curso.security.repository.projecoes.HistoricoPaciente;

@Service
public class AgendamentoService 
{	@Autowired private AgendamentoRepository agendamentoRepository;
	@Autowired Datatables datatables;

	@Transactional(readOnly=true)
	public List<Horario> buscarHorariosDisponiveisPorMedico(
			Long id, LocalDate date) 
	{return this.agendamentoRepository.buscarAgendamentoMedico(id, date);}

	@Transactional(readOnly=false)
	public void salvar(Agendamento agendamento) 
	{this.agendamentoRepository.save(agendamento);}

	/*Toda vez que um retorno vai para um datatable, devemos
	 *retornar um tipo Map*/
	@Transactional(readOnly=true)
	public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, 
			HttpServletRequest request) 
	{	this.datatables.setRequest(request);
		this.datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = this.agendamentoRepository.findHistoricoByPacienteEmail(
				email, this.datatables.getPageable());
		return this.datatables.getResponse(page);
	}

	@Transactional(readOnly=true)
	public Map<String, Object> buscarHistoricoPorMedicoEmail(String email, 
			HttpServletRequest request) 
	{	this.datatables.setRequest(request);
		this.datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
		Page<HistoricoPaciente> page = this.agendamentoRepository.findHistoricoByMedicoEmail(email, 
				this.datatables.getPageable());
		return this.datatables.getResponse(page);
	}

	@Transactional(readOnly=true)
	public Agendamento buscarAgendamentoPorId(Long id) 
	{return this.agendamentoRepository.findById(id).get();}
	
	
	/*O paciente é o unico objeto que não será alterado em toda
	 *operação, portanto, não precisamos fazer nenhum SET para
	 *paciente.*/
	@Transactional(readOnly=false)
	public void editar(Agendamento agendamento, String username) 
	{	Agendamento ag = this.buscarAgendamentoPorId(agendamento.getId());
		ag.setDataConsulta(agendamento.getDataConsulta());
		ag.setEspecialidade(agendamento.getEspecialidade());
		ag.setHorario(agendamento.getHorario());
		ag.setMedico(agendamento.getMedico());
	}

	@Transactional(readOnly=true)
	public Agendamento buscarAgendamentoPorIdAndUser(Long id, String email) 
	{	return this.agendamentoRepository.findByIdAndUserName(id, email)
			.orElseThrow(() -> new AccessDeniedException("Acesso negado! " + email));
	}
}