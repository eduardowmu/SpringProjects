package com.edu.exemplo.boot.dao;

import org.springframework.stereotype.Repository;

import com.edu.exemplo.boot.domain.Cargo;

@Repository
public class CargoDAO extends AbstractDAO<Cargo, Long> 
	implements ICargoDAO	{}
