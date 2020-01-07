package com.edu.exemplo.boot.dao;

import java.util.List;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Departamento;

public interface ICargoDAO 
{	public void save(Cargo cargo);
	public void update(Cargo cargo);
	public void delete(Long id);
	public Cargo findById(Long id);
	public List<Cargo> findAll();
}