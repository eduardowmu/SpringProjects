package com.mballem.curso.security.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mballem.curso.security.domain.Usuario;
/*não iremos testar a senha pois esta é automaticamente testada
pelo próprio spring. então um determinado método que vamos usar
mais a frente, o spring vai pegar nossa consulta pelo email e depois
mesmo testa a senha vamos agora, criar aqui no pacote service a classe
usuario senha service*/
public interface UserRepository extends JpaRepository<Usuario, Long> 
{	@Query("select u from Usuario u where u.email like :email")
	Usuario findByEmail(@Param("email") String email);
	
	/*Consulta por dois valores*/
	@Query("select u from Usuario u join u.perfis p where "
			+ "u.email like :search% OR p.desc like :search%")
	Page<Usuario> findByEmailOrPerfil(String search, Pageable pageable);
	
	/*Usaremos o IN para testar uma lista*/
	@Query("select u from Usuario u join u.perfis p where " + 
			"u.id = :usuarioId AND p.id IN :perfisId")
	Optional<Usuario> findByIdAndProfile(Long usuarioId, Long[] perfisId);
}