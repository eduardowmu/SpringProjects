package com.edu.exemplo.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.exemplo.boot.dao.DepartamentoDAO;
import com.edu.exemplo.boot.domain.Departamento;

@Service
public class DepartamentoService implements IDepartamentoService 
{	@Autowired private DepartamentoDAO dao;
	
	@Transactional(readOnly = false)
	@Override public void salvar(Departamento departamento) {dao.save(departamento);}
	
	@Transactional(readOnly = false)
	@Override public void editar(Departamento departamento) {dao.update(departamento);}
	
	@Transactional(readOnly = false)
	@Override public void excluir(Long id) {dao.delete(id);}
	
	@Override @Transactional(readOnly = true)
	public Departamento buscarPorId(Long id) {return dao.findById(id);}
	
	@Override @Transactional(readOnly = true)
	public List<Departamento> listarTodos() {return dao.findAll();}
	
	@Override public boolean temCargo(Long id) 
	{	if(this.buscarPorId(id).getCargos().isEmpty())
		{return false;}
		
		return true;
	}
}