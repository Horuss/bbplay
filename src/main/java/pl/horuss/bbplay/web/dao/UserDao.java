package pl.horuss.bbplay.web.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pl.horuss.bbplay.web.model.User;

public interface UserDao extends CrudRepository<User, Long> {

	@Query(value = "SELECT u FROM User u LEFT JOIN FETCH u.roles where u.name = :name")
	public User findByNameWithRoles(@Param("name") String name);

}
