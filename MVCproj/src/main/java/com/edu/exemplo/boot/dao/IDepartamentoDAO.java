package com.edu.exemplo.boot.dao;

import java.util.List;

import com.edu.exemplo.boot.domain.Departamento;

public interface IDepartamentoDAO 
{	public void save(Departamento depto);
	public void update(Departamento depto);
	public void delete(Long id);
	public Departamento findById(Long id);
	public List<Departamento> findAll();
}
