package br.edu.udemy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.edu.udemy.entity.User;
import br.edu.udemy.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController 
{	@Autowired private UserRepository repository;
	
	@RequestMapping(value="", method = RequestMethod.GET)
	public List<User> getUsers()
	{return this.repository.findAll();}
	
	@RequestMapping(value="/save", method=RequestMethod.POST)
	public User save(@RequestBody User user)
	{return this.repository.save(user);}
	
	@RequestMapping(value="/update", method=RequestMethod.PUT)
	public User update(@RequestBody User user)
	{return this.repository.save(user);}
	
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	public List<User> deleteUser(@RequestBody User user)
	{	this.repository.delete(user);
		return this.repository.findAll();
	}
}