package br.edu.udemy.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.edu.udemy.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, Long>
{	public User findByName(String name);
	/*
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User findByEmail(@Param("email") String email);*/
}
