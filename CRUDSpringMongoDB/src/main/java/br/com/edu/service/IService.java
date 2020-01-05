package br.com.edu.service;

import java.util.List;
import java.util.Optional;

import br.com.edu.domain.Client;

public interface IService 
{	List<Client> listAll();
	Client searchClientID(String id);
	Client save(Client client);
	Client update(Client client);
	void delete(String id);
}
