package br.edu.fatec.topicos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.fatec.topicos.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{	//@Query("SELECT u FROM usuario u WHERE :login = u.logjn")
	public Optional<Usuario> findByLogin(String login);

}
