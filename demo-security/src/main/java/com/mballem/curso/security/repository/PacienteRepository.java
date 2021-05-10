package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long>
{	/*Metodo que retorna um OPtional para o tipo paciente*/
	@Query("select p from Paciente p where p.usuario.email like :email")
	Optional<Paciente> findByUsuarioEmail(String email);
}
