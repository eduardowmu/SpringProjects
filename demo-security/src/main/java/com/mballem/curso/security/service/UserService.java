package com.mballem.curso.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UserRepository;

@Service
public class UserService 
{	@Autowired private UserRepository userRepository;
	
	//método que busca o usuario, que busca o retorno
	//do repositório
	public Usuario pullByEmail(String email)
	{return this.userRepository.findByEmail(email);}
}
