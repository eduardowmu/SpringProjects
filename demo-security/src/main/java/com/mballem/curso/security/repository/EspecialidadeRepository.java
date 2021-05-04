package com.mballem.curso.security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Usuario;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> 
{	/*query de consulta em JPQL, pelo titulo da especialidade. O search é o nome
 	da variável que está trazendo*/
	@Query("select e from Especialidade e where e.titulo like :search%")
	Page<Especialidade> findAllByTitulo(String search, Pageable pageable);
	
	@Query("select e.titulo from Especialidade e where e.titulo like :termo%")
	List<String> findEspecialidadesByTermo(String termo);
	
	/*Como iremos fazer consultas baseadas em arrays, usamos a clausura IN*/
	@Query("select e from Especialidade e where e.titulo IN :titulos")
	Set<Especialidade> findByTitulos(String[] titulos);
}