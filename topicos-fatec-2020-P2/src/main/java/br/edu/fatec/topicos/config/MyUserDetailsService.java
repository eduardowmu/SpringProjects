package br.edu.fatec.topicos.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.edu.fatec.topicos.entity.Papel;
import br.edu.fatec.topicos.entity.Usuario;
import br.edu.fatec.topicos.repository.UsuarioRepository;

//Este serviço tem um unico papel, que é buscar usuario(s) do banco de dados
@Service
public class MyUserDetailsService implements UserDetailsService 
{	@Autowired  private UsuarioRepository repository;
	@Override @Transactional 
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException 
	{	Optional<Usuario> usuarioOptional = this.repository.findByLogin(username);
		
		if(usuarioOptional.isPresent())
		{	Usuario usuario = usuarioOptional.get();
			
			List<GrantedAuthority> authorities = new ArrayList<>();
			
			for(Papel papel:usuario.getPapeis())
			{authorities.add(new SimpleGrantedAuthority(papel.getNome()));}
		
			return new User(username, usuario.getSenha(), authorities);
		}
	
		else throw new UsernameNotFoundException("Usuario não encontrado");
	}
}