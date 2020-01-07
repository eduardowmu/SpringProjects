/**
 * 
 */
package com.edu.exemplo.boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edu.exemplo.boot.dao.CargoDAO;
import com.edu.exemplo.boot.domain.Cargo;

/**
 * @author vilt-
 *
 */		 //anotação do springframework
@Service @Transactional(readOnly = false)
public class CargoService implements ICargoService
{	@Autowired//DAO que será automaticamente instanciada
	private CargoDAO dao;//para realizar os serviços chamados
	
	@Override public void salvar(Cargo cargo) {dao.save(cargo);}

	@Override public void editar(Cargo cargo) {dao.update(cargo);}

	@Override public void excluir(Long id) {dao.delete(id);}
	
	@Override @Transactional(readOnly = true)
	public Cargo buscarPorId(Long id) {return dao.findById(id);}

	@Override @Transactional(readOnly = true)
	public List<Cargo> listarTodos() {return dao.findAll();}
	
	@Override public boolean temFuncionario(Long id) 
	{	if(this.buscarPorId(id).getFuncionarios().isEmpty())
		{return false;}
		
		return true;
	}
}