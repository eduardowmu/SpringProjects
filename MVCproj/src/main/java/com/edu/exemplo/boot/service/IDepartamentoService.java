package com.edu.exemplo.boot.service;

import java.util.List;

import com.edu.exemplo.boot.domain.Cargo;
import com.edu.exemplo.boot.domain.Departamento;

public interface IDepartamentoService 
{	public void salvar(Departamento departamento);
	public void editar(Departamento departamento);
	public void excluir(Long id);
	public Departamento buscarPorId(Long id);
	public List<Departamento> listarTodos();
	public boolean temCargo(Long id);
}