package br.edu.udemy.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
/*
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;*/

import br.edu.udemy.entity.User;
import br.edu.udemy.repository.UserRepository;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent>
{	@Autowired UserRepository repository; 
	
	@Override public void onApplicationEvent(ContextRefreshedEvent event) 
	{	/*List<User> users = this.repository.findAll();
		
		if(users.isEmpty())	
		{	this.createUser("Leonardo", "leonardo@gmail.com");
			this.createUser("Fabio", "fabio@gmail.com");
			this.createUser("Maria", "maria@gmail.com");
		}
		
		//User user = this.repository.getOne(1L);
		
		System.out.println(this.repository.findByName("Leonardo"));*/
	}
	
	public void createUser(String name, String email)
	{	User user = new User(name, email);
		this.repository.save(user);
	}
}