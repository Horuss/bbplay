package pl.horuss.bbplay.web.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.model.Step;

public interface StepDao extends CrudRepository<Step, Long> {

	@Query(value = "SELECT DISTINCT s FROM Step s JOIN FETCH s.entities where s.play = :play")
	public Iterable<Step> findAllByPlayWithEntities(@Param("play") Play play);

}
