package com.edu.exemplo.boot.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.edu.exemplo.boot.domain.Funcionario;

@Repository public class FucionarioDAO extends AbstractDAO<Funcionario, Long> implements IFuncionarioDAO
{	public List<Funcionario> findByNome(String nome)
	{//uma consulta que localiza qq usuário pelo nome indepentemente do que foi digitado no formulário
		return createQuery("SELECT f FROM Funcionario f WHERE f.nome like concat('%', ?1, '%')", nome);
	}

	public List<Funcionario> findByCargo(Long id)
	{return createQuery("SELECT f from Funcionario f WHERE f.cargo.id = ?1", id);}
	//quando houver ambas as datas.
	public List<Funcionario> findByDates(LocalDate entrada, LocalDate saida)
	{	return createQuery("SELECT f FROM Funcionario f WHERE f.dataEntrada >= ?1 "
				+ "AND f.dataSaida <= ?2 ORDER BY f.dataEntrada ASC", entrada, saida);
	}
	//quando houver apenas data de entrada
	public List<Funcionario> findByInDate(LocalDate entrada)
	{	return createQuery("SELECT f FROM Funcionario f WHERE f.dataEntrada = ?1 "
			+ "ORDER BY f.dataEntrada ASC", entrada);
	}
	//quando houver apenas data de saída
	public List<Funcionario> findByEndDate(LocalDate saida)
	{	return createQuery("SELECT f FROM Funcionario f WHERE f.dataSaida = ?1 "
			+ "ORDER BY f.dataSaida ASC", saida);
	}
}