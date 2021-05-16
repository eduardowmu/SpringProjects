package com.mballem.curso.security.web.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Agendamento;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.AgendamentoService;
import com.mballem.curso.security.service.EspecialidadeService;
import com.mballem.curso.security.service.PacienteService;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController 
{	@Autowired private AgendamentoService agendamentoService;
	@Autowired private PacienteService pacienteService;
	@Autowired private EspecialidadeService especialidadeService;
	
	@GetMapping({"/agendar"})
	public String agendarConsulta(Agendamento agendamento)
	{return "agendamento/cadastro";}
	
	@GetMapping({"/horario/medico/{id}/data/{data}"})
	public ResponseEntity<?> buscarHorarios(@PathVariable("id") Long id, 
			@PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate date)
	{	/*Para o parametro data teremos que fazer uma conversão, pois
	 	estamos enviando do front uma string*/
		return ResponseEntity.ok(this.agendamentoService.buscarHorariosDisponiveisPorMedico(id, date));
	}
	
	/*o objeto agendamento é quem vai trazer as informações da página
	 *O REdirectAttributes é para envirarmos uma mensagem de resposta
	 *para o lado cliente e o Authentication é para que saibamos qual
	 *usuário está fazendo a requisição.*/
	@PostMapping({"/salvar"})
	public String agendar(Agendamento agendamento, 
		RedirectAttributes attr, @AuthenticationPrincipal User user)
	{	Paciente paciente = this.pacienteService.buscarPorPacienteByEmail(user.getUsername());
		/*Quando enviamos a especialidade da página para a controller, esta vindo apenas
		 *com o titulo, sem o ID. Para que salvemos depois na tabela de agendamentos, vamos
		 *precisar do ID da especialidade.*/
		String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = this.especialidadeService
				/*Poderíamos até criar um método que   retornasse apenas um resultado*/
				.buscarPorTitulos(new String[] {titulo})
				/*Este recurso existe apenas a partir do java 8*/
				.stream()
				/*Como o metodo findFirst retorna um tipo Optional, devemos chamar
				 *o get() para obtermos o titulo com ID desejado*/
				.findFirst().get();
		agendamento.setEspecialidade(especialidade);
		agendamento.setPaciente(paciente);
		/*Não precisamos enviar o médico, pois quem envia na verdade é o combobox
		 *que passa o ID do médico, e se temos esse ID a JPA consegue fazer os
		 *relacionamento entre médicos e agendamentos apenas pelo ID do médico.
		 *Se estivéssemos enviando o nome do médico ao invés do ID, aí sim teríamos
		 *que fazer esse SET*/
		this.agendamentoService.salvar(agendamento);
		attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
		return "redirect:/agendamentos/agendar";
	}
	
	/*Abrir historico de agendamentos. O segundo é o link para o perfil de médicos*/
	@GetMapping({"/historico/paciente", "/historico/consultas"})
	public String historico() {return "agendamento/historico-paciente";}
	
	/*URI que temos no datatable*/
	@GetMapping("/datatables/server/historico")
	public ResponseEntity<?> historicoAgendamentosPorPaciente(HttpServletRequest request, 
			@AuthenticationPrincipal User user)
	{	/*Esse retorno envia para a página um http status do tipo 404, que teoricamente,
	 	este não deve acontecer durante o processo, pois teremos dentro do método duas
	 	operações que terão seus próprios retornos. Portanto é bem provável que caia no
	 	NOTFOUND. No método getAuthorities() temos a lista de perfis de usuários logados*/
		if(user.getAuthorities().contains(new SimpleGrantedAuthority(
				PerfilTipo.PACIENTE.getDesc())))
		{	return ResponseEntity.ok(this.agendamentoService.buscarHistoricoPorPacienteEmail(
				user.getUsername(), request));
		}
		
		if(user.getAuthorities().contains(new SimpleGrantedAuthority(
				PerfilTipo.MEDICO.getDesc())))
		{	return ResponseEntity.ok(this.agendamentoService.buscarHistoricoPorMedicoEmail(
				user.getUsername(), request));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping({"/editar/consulta/{id}"})
	public String preEditarConsultaPaciente(@PathVariable("id") Long id, ModelMap model,
			@AuthenticationPrincipal User user)
	{	/*Se fizermos esse tratamento também pelo email do usuário, iremos garantir
		 *que quem vai abrir a página do agendamento é o próprio usuário que está 
		 *logado na página e não um outro usuário.*/
		Agendamento agendamento = this.agendamentoService.buscarAgendamentoPorIdAndUser(id, 
				user.getUsername());
		
		/*o objeto é recuperado no banco de dados e é enviado para a página*/
		model.addAttribute("agendamento", agendamento);
		return "agendamento/cadastro";
	}
	
	@PostMapping("/editar")
	public String editarConsulta(Agendamento agendamento, RedirectAttributes attr,
			@AuthenticationPrincipal User user)
	{	String titulo = agendamento.getEspecialidade().getTitulo();
		Especialidade especialidade = this.especialidadeService
		.buscarPorTitulos(new String[] {titulo}).stream()
		.findFirst().get();
		agendamento.setEspecialidade(especialidade);
		this.agendamentoService.editar(agendamento, user.getUsername());
		attr.addFlashAttribute("sucesso", "Consulta editada com sucesso!");
		return "redirect:/agendamentos/agendar";
	}
	
	//esse path é tirado de agendamento.js
	@GetMapping("/excluir/consulta/{id}")
	public String excluirAgendamento(@PathVariable("id") Long id, RedirectAttributes attr)
	{	this.agendamentoService.delete(id);
		attr.addFlashAttribute("sucesso", "Agendamento excluído com sucesso");
		return "redirect:/agendamentos/historico/paciente";
	}
}