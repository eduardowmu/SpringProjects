package com.mballem.curso.security.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.datatables.Datatables;
import com.mballem.curso.security.datatables.DatatablesColunas;
import com.mballem.curso.security.domain.Especialidade;
import com.mballem.curso.security.repository.EspecialidadeRepository;
@Service
public class EspecialidadeService 
{	@Autowired private EspecialidadeRepository repository;
	@Autowired private Datatables dataTables;
	
	@Transactional(readOnly=false)
	public void salvar(Especialidade especialidade) 
	{this.repository.save(especialidade);}
	
	@Transactional(readOnly=true)
	public Map<String, Object> buscarEspecialidades(
			HttpServletRequest request) 
	{	this.dataTables.setRequest(request);
		this.dataTables.setColunas(
				/*Constantes que temos na classe
				 *DataTables das Especialidades*/
				DatatablesColunas.ESPECIALIDADES);
		/*Método verificado se esta vazio, se estiver vazio,
		 *significa que o usuário fez uma busca sem preencher
		 *os campos de busca.*/
		Page<?> page = this.dataTables.getSearch().isEmpty() ? 
				this.repository.findAll(this.dataTables.getPageable()):
				this.repository.findAllByTitulo(this.dataTables.getSearch(),
				this.dataTables.getPageable());
		return this.dataTables.getResponse(page);
	}
	
	@Transactional(readOnly=true)
	public Especialidade buscarPorId(Long id) 
	{	/*O método findById() retorna um Optional<T>
	 	que no caso, o T = Especialidade. Portanto,
	 	devemos usar o método get() para retornarmos
	 	o objeto do tipo desejado, ou seja, Especialidade*/
		return this.repository.findById(id).get();
	}
	
	@Transactional(readOnly=false)
	public void removerPorId(Long id) {this.repository.deleteById(id);}

	@Transactional(readOnly=true)
	public List<String> buscarEspecialidadesPorTermo(String termo) 
	{return this.repository.findEspecialidadesByTermo(termo);}
	
	public Set<Especialidade> buPorTitulos(String[] titulos) 
	{return this.repository.findByTitulos(titulos);}
}