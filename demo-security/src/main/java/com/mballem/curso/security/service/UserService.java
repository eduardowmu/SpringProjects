package com.mballem.curso.security.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.exception.AccessDeniedException;
import com.mballem.curso.security.repository.UserRepository;

@Service
public class UserService implements UserDetailsService
{	@Autowired private UserRepository userRepository;

	@Autowired private Datatables dataTables;
	
	/*Dependência para serviço de e-mail para confirmação de cadastro.*/
	@Autowired private EmailService emailService;
	
	//método que busca o usuario, que busca o retorno do repositório.
	//a notação faz parte do processo de transações do
	//spring. Este valor true significa que esta sendo
	//gerenciado pelo processo de transação do spring.
	//quando temos o retorno do objeto usuário, esse
	//esse sistema de transações que gerenciou o método
	//buscar por e-mail é encerrado, quando utilizamos
	//o método getPerfis() abaixo dentro do loadUserByUsername
	//como o sistema transacional do spring ja encerrou
	//com o método buscar por e-mail, não temos mais acesso
	//a aquela sessão que foi criado com banco de dados
	//então, não consegue voltar com o banco de dados e trazer a lista de perfis.
	@Transactional(readOnly = true)
	public Usuario pullByEmail(String email)
	{return this.userRepository.findByEmail(email);}
	
	/*Para solucionar esse problema, adicionamos essa notação também*/
	@Transactional(readOnly = true)
	@Override public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException 
	{	//obtendo a resposta da nossa consulta. Um objeto que fica
		//na sessão e a partir deste objeto, o spring secuirty consegue
		//comparar se o perfil do objeto que está na sessão é igual ao
		//perfil que estamos dando a autorização em cada uma das URLs.
		Usuario usuario = //this.pullByEmail(username);
				this.buscarPorEmailEAtivo(username)
			.orElseThrow(() -> new UsernameNotFoundException(
				"Usuario "+username+" não encontrado"));
		
		/*User é uma classe do Spring que implementa 
		 *UserDetails. É com isso que iremos conseguir
		 *passar alguns dos dados que recuperamos a partir
		 *da consulta*/
		return new User(usuario.getEmail(), usuario.getSenha(),
				//classe do Spring que possui alguns métodos
				//uma delas é a criação de uma lista de regras,
				//onde iremos configurar os perfis de cada usuário
				AuthorityUtils.createAuthorityList(
						this.getAuthorities(usuario.getPerfis())));
	}
	
	private String[] getAuthorities(List<Perfil> perfis)
	{	String[] authorities = new String[perfis.size()];
		for(int i = 0; i < perfis.size(); i++)
		{authorities[i] = perfis.get(i).getDesc();}
		
		return authorities;
	}
	
	/*Começando a trabalhar com DataTables*/
	@Transactional(readOnly = true)
	public Map<String, Object> bucarUsuarios(HttpServletRequest request) 
	{	this.dataTables.setRequest(request);
		this.dataTables.setColunas(DatatablesColunas.USUARIOS);
		/*Trabalhando com objeto page, que é o resultado
		 *que obteremos a partir da consulta que será feita
		 *via spring data JPA*/
		Page<Usuario> page = this.dataTables.getSearch().isEmpty() ?
		/*se estiver vazio, faremos uma consulta básica do tipo findAll()
		 *que fará apenas ter recursos de paginação e ordenação.*/
				this.userRepository.findAll(this.dataTables.getPageable()) :
				/*se não, teremos um parametro, que servirá de filtro, 
				 *que será para que possamos filtrar nossos usuarios*/
				this.userRepository.findByEmailOrPerfil(
						this.dataTables.getSearch(), 
						this.dataTables.getPageable());
		return this.dataTables.getResponse(page);
	}

	@Transactional(readOnly = false)
	public void save(Usuario usuario) 
	{	/*Antes de salvar, precisamos criptografar a senha do usuário*/
		String cryptPass = new BCryptPasswordEncoder().encode(
			usuario.getSenha());
		usuario.setSenha(cryptPass);
		this.userRepository.save(usuario);
	}
	
	@Transactional(readOnly = true)
	public Usuario findById(Long id) 
	{return this.userRepository.findById(id).get();}
	
	
	@Transactional(readOnly = true)
	public Usuario findByIdAndPerfis(Long id, Long[] perfisId) 
	{	return this.userRepository.findByIdAndProfile(id, perfisId)
			/*Este método vai tratar o seguinte: Se existir um usuário dentro 
			 *do Optional, retona um usuário, desde que a consulta tenha retornado 
			 *dados para o mesmo objeto usuário. Caso contrário, irá lançar uma 
			 *exception, que deve ser num formato lambda*/
			.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	}

	public static boolean isSenhaCorreta(String senhaDigitada, String senhaAtual) 
	{	/*Lembrando que a senha atual deve ser a que esta criptografada*/
		return new BCryptPasswordEncoder().matches(senhaDigitada, senhaAtual);
	}

	@Transactional(readOnly = false)
	public void editSenha(Usuario usuario, String senha) 
	{	usuario.setSenha(new BCryptPasswordEncoder().encode(senha));
		this.userRepository.save(usuario);
	}

	@Transactional(readOnly = false)
	public void salvarCadastroPaciente(Usuario usuario) throws MessagingException 
	{	//Criptografia de senha
		String cripty = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(cripty);
		usuario.addPerfil(PerfilTipo.PACIENTE);
		this.userRepository.save(usuario);
		/*chamando o metodo de serviço de envio de e-mail*/
		this.enviarEmailCadastro(usuario.getEmail());
	}
	
	@Transactional(readOnly = true)
	public Optional<Usuario> buscarPorEmailEAtivo(String email)
	{return this.userRepository.findByEmailAndAtivo(email);}
	
	/*Método de envio de serviço de e-mail após salvar o cadastro do usuário.
	 *Além do envio de e-mail, este carregará um código, para que consiga fazer
	 *a confirmação do cadastro, enviando como um parametro da URL, pois quando
	 *chegar o e-mail para o paciente, e clicar no link, este deve conter o e-mail
	 *para que a aplicação receba essa requisição e possamos ativar o cadastro
	 *a partir deste e-mail. Esse código é do tipo base64*/
	public void enviarEmailCadastro(String email) throws MessagingException
	{	/*criação do código de confirmação, cujo método recebe como parametros
		um array de bytes*/
		String codigo = Base64Utils.encodeToString(email.getBytes());
		this.emailService.enviarPedidoConfirmacaoCadastro(email, codigo);
	}
	
	/*Método de ativação do cadastro, esse codigo precisa novamente ser convertido
	 *no e-mail do usuário*/
	@Transactional(readOnly = false)
	public void ativarCadastroPaciente(String codigo)
	{	String email = new String(Base64Utils.decodeFromString(codigo));
		
		/*Criando um objeto do tipo usuario, que será o dono deste e-mail*/
		Usuario usuario = this.pullByEmail(email);
		
		/*Condição para caso o usuário não for encontrado, será lançado uma exceção*/
		if(usuario.hasNotId())
		{	throw new AccessDeniedException("Não foi possível ativar seu cadastro. "
				+ "Entre em contato com suporte");
		}
		
		/*Caso o usuário exista, ajusta seu cadastro como ativo. Isso o hibernate já
		 *faz de forma automática se fazermos apenas um setAtivo(true) do método da
		 *classe*/
		usuario.setAtivo(true);
	}

	@Transactional(readOnly = false)
	public void pedidoRedefinicaoSenha(String email) throws MessagingException 
	{	/*Será usado este método pois o mesmo precisa estar como ativo no cadastro*/
		Usuario usuario = this.buscarPorEmailEAtivo(email)
			.orElseThrow(() -> new UsernameNotFoundException("Usuário " + email
					+ "não encontrado"));
		
		/*Gerando o código verificardor. Essa Classe randomica tem um método que é
		 *alpha numérico, que recebe como parametro o numero de caracateres que
		 *definirmos*/
		String verificador = RandomStringUtils.randomAlphanumeric(6);
		
		/*configurando o codigo verificador do usuário. Assim iremos então realizar
		 *o update no banco de dados.*/
		usuario.setCodigoVerificador(verificador);
		
		/*Fazendo o envio de email para recuperação de senha*/
		this.emailService.enviarPedidoRedefinicaoSenha(email, verificador);
	}
}