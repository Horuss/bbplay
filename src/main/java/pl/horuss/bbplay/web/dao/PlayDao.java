package pl.horuss.bbplay.web.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import pl.horuss.bbplay.web.model.Play;

public interface PlayDao extends CrudRepository<Play, Long> {

	List<Play> findByPublished(boolean published);

}
