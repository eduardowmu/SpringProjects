package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeRepository;
@Service
public class EspecialidadeService 
{	@Autowired private EspecialidadeRepository repository;
	
	@Transactional(readOnly=false)
	public void salvar(Especialidade especialidade) 
	{this.repository.save(especialidade);}
}