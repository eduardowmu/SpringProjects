package com.edu.exemplo.boot.dao;

import org.springframework.stereotype.Repository;

import com.edu.exemplo.boot.domain.Departamento;

@Repository
public class DepartamentoDAO extends AbstractDAO<Departamento, Long> 
	implements IDepartamentoDAO{}