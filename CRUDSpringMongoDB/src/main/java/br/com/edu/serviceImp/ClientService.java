package br.com.edu.serviceImp;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.edu.domain.Client;
import br.com.edu.repository.IClientRepository;
import br.com.edu.service.IService;
//para que o Spring identifique é um serviço e para que seja add em outras aplicações
@Service
public class ClientService implements IService 
{	@Autowired
	private IClientRepository cr;					/*cria se automaticamente uma instancia não 
													nula de cliente repositório*/
	@Override 
	public List<Client> listAll()	{return this.cr.findAll();}
	
	@Override
	public Client searchClientID(String id) 
	{return this.cr.findOne(id);}
	
	@Override
	public Client save(Client client) {return this.cr.save(client);}

	/*o método update é usado o mesmo método de salvar, com a diferença
	 de que, se o objeto já tiver ID, será feito o atualizar. Caso contrário,
	 irá identificar que é um objeto novo e será feito um cadastrar*/
	@Override
	public Client update(Client client)	{return this.cr.save(client);}
	
	@Override
	public void delete(String id)	{this.cr.delete(id);}
}