package pl.horuss.bbplay.web.dao;

import org.springframework.data.repository.CrudRepository;

import pl.horuss.bbplay.web.model.Player;

public interface PlayerDao extends CrudRepository<Player, Long> {

}
