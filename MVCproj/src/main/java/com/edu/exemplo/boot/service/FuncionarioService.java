package com.edu.exemplo.boot.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.edu.exemplo.boot.dao.FucionarioDAO;
import com.edu.exemplo.boot.domain.Funcionario;

@Service
public class FuncionarioService implements IFuncionarioService 
{	@Autowired private FucionarioDAO dao;
	
	//os métodos de escrita precisa setar o readOnly como false, pois caso contrário
	@Transactional(readOnly = false)//será gerado um erro de execeção
	@Override public void salvar(Funcionario funcionario) {dao.save(funcionario);}
	
	@Transactional(readOnly = false)
	@Override public void editar(Funcionario funcionario) {dao.update(funcionario);}
	
	@Transactional(readOnly = false)
	@Override public void excluir(Long id) {dao.delete(id);}

	@Override @Transactional(readOnly = true)
	public Funcionario buscarPorId(Long id) {return dao.findById(id);}

	@Override @Transactional(readOnly = true)
	public List<Funcionario> listarTodos() {return dao.findAll();}

	public List<Funcionario> buscarPorNome(String nome) 
	{return dao.findByNome(nome);}

	@Override public List<Funcionario> findByNome(String nome) 
	{return dao.findByNome(nome);}

	@Override public List<Funcionario> buscarPorCargo(Long id) 
	{return dao.findByCargo(id);}

	@Override public List<Funcionario> buscarPorDatas(LocalDate entrada, LocalDate saida) 
	{	if(entrada != null && saida != null)	return dao.findByDates(entrada, saida);
	
		else if(entrada != null)	return dao.findByInDate(entrada);
	
		else if(saida != null)	return dao.findByEndDate(saida);
	
		else return new ArrayList<>();
	}	
}