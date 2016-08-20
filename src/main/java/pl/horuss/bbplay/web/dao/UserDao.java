package pl.horuss.bbplay.web.dao;

import org.springframework.data.repository.CrudRepository;

import pl.horuss.bbplay.web.model.User;

public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByName(String name);

}
