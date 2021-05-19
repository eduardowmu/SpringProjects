package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UserService;

@Controller
@RequestMapping("u")
public class UserController 
{	@Autowired private UserService service;
	@Autowired private MedicoService medService;

	//abrir cadastro de usuarios (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario)
	{return "usuario/cadastro";}
	
	/*abrir lista de usuarios*/
	@GetMapping("/lista")
	public String listarUsuarios()
	{return "usuario/lista";}
	
	/*listar usuarios na datatables (usuario.js - ajax table-usuarios)*/
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<Map<String, Object>> listarUsuariosDataTable(HttpServletRequest request)
	{return ResponseEntity.ok(this.service.bucarUsuarios(request));}
	
	/*Teremos o RedirectAttributes para que tenhamos a resposta depois
	 *a partir de uma redirect através deste método. O parâmetro da nota -
	 *ção é o mesmo que temos no formulário HTML de cadastro. Este método
	 *serve para cadastro de usuarios por ADMIN, pois teremos este método
	 *que irá salvar os cadastros de novos usuários que serão as credenciais
	 *de novos usuários lá pelo lado de ADMIN. Então novos ADMINs serão
	 *criados por este sistema de cadastro e os médicos também serão criados
	 *por este sistema de cadastro. O unico usuário que irá criar seu próprio
	 *cadastro será o paciente, pois assim poderá fazer seu cadastro pelo
	 *próprio site, antes de estar logado no site. Mas um médico, por ser um 
	 *funcionário da clínica, quem irá cadastrar nesse médico no sistema será um ADMIN.*/
	@PostMapping("/cadastro/salvar")
	public String saveUser(Usuario usuario, RedirectAttributes attr)
	{	List<Perfil> perfis = usuario.getPerfis();
		/*Condição que verifica se as regras de cadastro estão sendo obedecidas, de acordo com os perfis.*/
		if(perfis.size() > 2 || //1L representa o ID de perfil do tipo ADMIN e 3L é o perfil de paciente.
			perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
			perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L))))
		{	//o primeiro parametro serve para que acessemos nosso fragmento de alerta
			attr.addFlashAttribute("falha:", "Cadastro não permitido de acordo"
					+ " com as normas internas.");
			attr.addFlashAttribute("usuario", usuario);
		}
		else//senão, cadastro permitido
		{	try
			{	this.service.save(usuario);
				attr.addFlashAttribute("sucesso", "Usuario salvo com sucesso");	
			}
			/*Se já houver usuário com o mesmo e-mail enviado pela requisição*/
			catch(DataIntegrityViolationException e)
			{	attr.addFlashAttribute("falha", "Cadastro não realizado. "
					+ "E-mail já existente");
			}
		}	//é o mesmo contexto do GetMapping acima
		return "redirect:/u/novo/cadastro/usuario";
	}
	
	//para pre-editar credenciais de usuários
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id)
	{	return new ModelAndView("usuario/cadastro", 
			//enviando os dados para a página de cadastro
			"usuario",
			//enviando os dados do usuário, a partir de um objeto usuário, que pegamos a partir de uma
			//consulta que precisa ser realizada pelo ID do usuário que estamos recebendo pela URL
			service.findById(id));
	}
	//pre edição de dados pessoais de acordo com perfil
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroPessoais(@PathVariable("id") Long id,
			@PathVariable("perfis") Long[] perfisId)
	{	Usuario user = this.service.findByIdAndPerfis(id, perfisId);//new Usuario();
		//restringindo o acesso do ADMIN e ao mesmo tempo médico 
		//ao perfil de usuário paciente. Aqui foi onde gerou a exceção
		//de 500, quando que na verdade deveria ser 404
		if(user.getPerfis().contains(
				new Perfil(PerfilTipo.ADMIN.getCod()))
				&& !user.getPerfis().contains(
				new Perfil(PerfilTipo.MEDICO.getCod())))
		{return new ModelAndView("usuario/cadastro", "usuario", user);}
		
		//restringindo acesso de um medico
		else if(user.getPerfis().contains(
				new Perfil(PerfilTipo.MEDICO.getCod())))
		{	/*Declarando um objeto de medico e precisaremos acessar
		 	a service de médico. Lembrando que se já tivermos um ID, 
		 	desse médico, será feito um update, mas senão, teremos um create*/
			Medico medico = this.medService.findUserById(id);
			//método de classe AbstractEntity do spring. Caso o médico 
			//ainda não exista, será criado um novo com o ID do usuário
			return medico.hasNotId() ? 
				new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(id))) : 
				new ModelAndView("medico/cadastro", "medico", medico);
			
			//return new ModelAndView("especialidade/especialidade");
		}
		
		//condição para paciente, mas aqui não iremos enviar a
		//requisição para a área de paciente. Iremos dizer para o
		//ADMIN que não terá acesso a essa área. Para isso usaremos
		//o metodo de acesso negado da HomeController
		else if(user.getPerfis().contains(
				new Perfil(PerfilTipo.PACIENTE.getCod())))
		{	ModelAndView mv = new ModelAndView("error");
			//para o tipo ModelAndView, usamos o objeto e não atributo
			mv.addObject("status", 403);
			mv.addObject("error", "Área Restrita!");
			mv.addObject("message", "Você não tem permissão para acesso a esta área ou ação");
			return mv;
		}
		
		return new ModelAndView("redirect:/u/lista");
	}
	
	/*Metodo utilizado para poder abrir a página da senmha no navegador*/
	@GetMapping("/editar/senha")
	public String abrirEditarSenha()	{return "usuario/editar-senha";}
	
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2, 
		@RequestParam("senha3") String s3, @AuthenticationPrincipal User user, RedirectAttributes attr)
	{	if(!s1.equals(s2))
		{	attr.addFlashAttribute("Falha:", "Senhas não batem! Tente novamente");
			return "redirect:/u/editar/senha";
		}
	
		//org.springframework.security.core.userdetails.User
		Usuario usuario = this.service.pullByEmail(user.getUsername());
		
		if(!UserService.isSenhaCorreta(s3, usuario.getSenha()))
		{	attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente");
			return "redirect:/u/editar/senha";
		}
		
		this.service.editSenha(usuario, s1);
		/*Troca de senha com sucesso*/
		attr.addFlashAttribute("sucesso", "Senha atualizada com sucesso");
		return "redirect:/u/editar/senha";
	}
	
	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario)	{return "cadastrar-se";}
	
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado(Usuario usuario)	{return "fragments/mensagem";}
	
	//recebe o form da página cadastrar-se
	@PostMapping("/cadastro/paciente/salvar")
	public String cadastrarPaciente(Usuario usuario, 
		/*Esta classe serve para trabalharmos com a validação backend e é isso que teremos
		 *de erro quando o usuário tentar se cadastrar no sistema com um nome de usuário
		 *já cadastrado. Primeiro vamos criar um método que irá cadastrar este usuário.
		 *O método estará presente em UsuarioService*/	
		BindingResult result) throws MessagingException
	{	try {this.service.salvarCadastroPaciente(usuario);}
		/*Essa exceção será disparada quando tentarmos inserir um usuário que já exista no banco de dados*/
		catch(DataIntegrityViolationException e) 
		{	result.reject("email", "Usuário já cadastrado!");
			return "cadastrar-se";
		}
		return "redirect:/u/cadastro/realizado";
	}
	
	/*Recebe a requisição de confirmação de cadastro do paciente. Este método tem como retorno um objeto 
	 *string pois os retorno será um redirect para a página de login. No GetMapping precisamos add a URI 
	 *que vai acessar esse método, definido no serviço de e-mail, no metodo de envio de confirmação*/
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, 										
			RedirectAttributes attr)
	{	this.service.ativarCadastroPaciente(codigo);
		attr.addFlashAttribute("alerta", "Sucesso.");
		attr.addFlashAttribute("titulo", "Cadastro ativado.");
		attr.addFlashAttribute("texto", "Parabéns, seu cadastro foi ativado!");
		attr.addFlashAttribute("subtexto", "Já pode realizar seu login");
		return "redirect:/login";
	}
}