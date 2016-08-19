package pl.horuss.bbplay.web.dao;

import org.springframework.data.repository.CrudRepository;

import pl.horuss.bbplay.web.model.Play;

public interface PlayDao extends CrudRepository<Play, Long> {

}
