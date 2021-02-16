package com.mballem.curso.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Usuario;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {}
