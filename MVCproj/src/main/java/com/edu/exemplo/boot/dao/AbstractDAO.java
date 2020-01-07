package com.edu.exemplo.boot.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

//classe de DAO genérico, onde teremos um parâmetro genérico "T", 
//que pode ser qualquer tipo de entidade e um "PK" representando o tipo
//da chave primária, que pode ser um LONG, Integer, etc.
public abstract class AbstractDAO<T, PK extends Serializable>
{	@SuppressWarnings("unchecked")
	private final Class<T> entityClass = 
		(Class<T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	//uma notação da especificação da JPA, em que esta classe será responsável pela
	//persistencia dos dados
	@PersistenceContext
	private EntityManager entityManager;

	//sómente acessível apenas para as classes pertencentes ao pacote de DAO.
	protected EntityManager getEntityManager() {return entityManager;}
	public void setEntityManager(EntityManager entityManager) {this.entityManager = entityManager;}

	public Class<T> getEntityClass() {return entityClass;}
	
	//métodos genéricos
	public void save(T entity)	{this.entityManager.persist(entity);}		//salvar
	public void update(T entity) {this.entityManager.merge(entity);}		//alterar
	public void delete(PK id)	{this.entityManager.remove(entityManager.getReference(entityClass, id));}	//deletar
	public T findById(PK id)	{return this.entityManager.find(entityClass, id);}	//procurar por id
	public List<T> findAll()												//listar todos
	{	return this.entityManager.createQuery("from " + 
			this.entityClass.getSimpleName(), this.entityClass).getResultList();
	}
	
	protected List<T> createQuery(String jpql, Object...params)
	{	TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
		for(int i  = 0; i < params.length; i++)
		{query.setParameter(i+1, params[i]);}
		return query.getResultList();
	}
}