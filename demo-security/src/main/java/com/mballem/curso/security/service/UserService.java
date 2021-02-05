package com.mballem.curso.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.repository.UserRepository;

@Service
public class UserService implements UserDetailsService
{	@Autowired private UserRepository userRepository;
	
	//método que busca o usuario, que busca o retorno
	//do repositório.
	//a notação faz parte do processo de transações do
	//spring
	@Transactional(readOnly = true)
	public Usuario pullByEmail(String email)
	{return this.userRepository.findByEmail(email);}

	@Override public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException 
	{	//obtendo a resposta da nossa consulta
		Usuario usuario = this.pullByEmail(username);
		
		/*User é uma classe do Spring que implementa 
		 *UserDetails. É com isso que iremos conseguir
		 *passar alguns dos dados que recuperamos a partir
		 *da consulta*/
		return new User(usuario.getEmail(), 
				usuario.getSenha(),
				//classe do Spring que possui alguns métodos
				//uma delas é a criação de uma lista de regras,
				//onde iremos configurar os perfis de cada usuário
				AuthorityUtils.createAuthorityList(
						this.getAuthorities(usuario.getPerfis())));
	}
	
	private String[] getAuthorities(List<Perfil> perfis)
	{	String[] authorities = new String[perfis.size()];
		for(int i = 0; i < perfis.size(); i++)
		{authorities[i] = perfis.get(i).getDesc();}
		
		return authorities;
	}
}