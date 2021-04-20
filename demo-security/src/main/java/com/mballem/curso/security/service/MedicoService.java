package com.mballem.curso.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class MedicoService 
{	@Autowired private MedicoRepository repository;
	
	@Transactional(readOnly = true)
	public Medico findUserById(Long id) 
	{	return this.repository.findByUsuarioId(id)
			/*caso não encontre o medico desejado, evita o NullPointer*/
			.orElse(new Medico());
	}
}
