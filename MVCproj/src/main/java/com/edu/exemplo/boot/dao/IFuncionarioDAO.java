package com.edu.exemplo.boot.dao;

import java.util.List;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Funcionario;

public interface IFuncionarioDAO 
{	public void save(Funcionario funcionario);
	public void update(Funcionario funcionario);
	public void delete(Long id);
	public Funcionario findById(Long id);
	public List<Funcionario> findAll();
	public List<Funcionario> findByNome(String nome);
	
}