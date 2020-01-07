package com.edu.exemplo.boot.service;

import java.time.LocalDate;
import java.util.List;

import com.edu.exemplo.boot.domain.Funcionario;


public interface IFuncionarioService 
{	public void salvar(Funcionario funcionario);
	public void editar(Funcionario funcionario);
	public void excluir(Long id);
	public Funcionario buscarPorId(Long id);
	public List<Funcionario> listarTodos();
	public List<Funcionario> findByNome(String nome);
	public List<Funcionario> buscarPorCargo(Long id);
	public List<Funcionario> buscarPorDatas(LocalDate entrada, LocalDate saida);
}