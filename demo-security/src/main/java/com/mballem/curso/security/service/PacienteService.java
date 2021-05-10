package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.repository.PacienteRepository;

@Service
public class PacienteService 
{	@Autowired private PacienteRepository repository;
	
	@Transactional(readOnly = true)
	public Paciente buscarPorPacienteByEmail(String email)
	{return this.repository.findByUsuarioEmail(email).orElse(new Paciente());}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente)	{this.repository.save(paciente);}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) 
	{	Paciente pac = this.repository.findById(paciente.getId()).get();
		pac.setNome(paciente.getNome());
		pac.setDtNascimento(paciente.getDtNascimento());
	}
}