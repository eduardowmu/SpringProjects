package com.mballem.curso.security.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class MedicoService 
{	@Autowired private MedicoRepository repository;
	
	@Transactional(readOnly = true)
	public Medico findUserById(Long id) 
	{	return this.repository.findByUsuarioId(id)
			/*caso não encontre o medico desejado, 
			 *evita o NullPointer*/
			.orElse(new Medico());
	}
	
	@Transactional(readOnly = false)
	public void salvar(Medico medico) 
	{this.repository.save(medico);}
	
	/*O interessante é que não precisamos fazer o método
	 *save, pois como criamos um objeto persistente, o hibernate
	 *já faz isso automaticamente.*/
	@Transactional(readOnly = false)
	public void editar(Medico medico) 
	{	//Criando um médico persistente
		Medico medicoEditar = this.repository.findById(
			medico.getId()).get();
		medicoEditar.setCrm(medico.getCrm());
		medicoEditar.setDtInscricao(medico.getDtInscricao());
		medicoEditar.setNome(medico.getNome());
		/*Teste que verifica se a lista de especialidades não
		 *está vazia, ou seja, se o usuário digitou a especialidade
		 *devemos inserir no banco de dados*/
		if(!medico.getEspecialidades().isEmpty())
		{	medicoEditar.getEspecialidades().addAll(
				medico.getEspecialidades());
		}
	}

	@Transactional(readOnly = true)
	public Medico findByEmail(String email) 
	{return this.repository.findByEmail(email).orElse(new Medico());}

	@Transactional(readOnly = false)
	public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) 
	{	Medico medico = this.repository.findById(idMed).get();
		/*Através da expressão lambda iremos acessar cada especialidade
		 *dentro da lista do médico, e remover aquele que conter o mesmo
		 *ID passado como parâmetro do método. Nem precisamos do repository
		 *para fazer essa deleção, o proprio hibernate vai entender esse
		 *fluxo.*/
		medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));
	}

	@Transactional(readOnly = true)
	public List<Medico> buscarMedicoPorEspecialidade(String titulo) 
	{return this.repository.findByMedicosPorEspecialidade(titulo);}
}