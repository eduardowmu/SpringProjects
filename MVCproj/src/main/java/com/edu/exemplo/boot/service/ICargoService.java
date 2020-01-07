package com.edu.exemplo.boot.service;

import java.util.List;

import com.edu.exemplo.boot.domain.Cargo;

public interface ICargoService 
{	public void salvar(Cargo cargo);
	public void editar(Cargo cargo);
	public void excluir(Long id);
	public Cargo buscarPorId(Long id);
	public List<Cargo> listarTodos();
	public boolean temFuncionario(Long id);
}
