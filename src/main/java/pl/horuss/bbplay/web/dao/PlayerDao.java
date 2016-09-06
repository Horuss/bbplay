package pl.horuss.bbplay.web.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import pl.horuss.bbplay.web.model.Player;

public interface PlayerDao extends CrudRepository<Player, Long> {

	@Query(value = "SELECT p FROM Player p LEFT JOIN FETCH p.user")
	public Iterable<Player> findAllWithUsers();

}
