package pl.horuss.bbplay.web.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.dao.PlayDao;
import pl.horuss.bbplay.web.dao.StepDao;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.model.Step;

@Service
public class PlaybookService {

	@Autowired
	private PlayDao playDao;

	@Autowired
	private StepDao stepDao;

	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	// @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public List<Play> getPlays() {
		return StreamSupport.stream(playDao.findAll().spliterator(), false).collect(
				Collectors.toList());
	}

	public List<Step> getSteps(Play play) {
		List<Step> collect = StreamSupport.stream(
				stepDao.findAllByPlayWithEntities(play).spliterator(), false).collect(
				Collectors.toList());
		play.setSteps(collect);
		return collect;
	}

	public Play save(Play play) {
		return playDao.save(play);
	}

	public void delete(Play play) {
		playDao.delete(play);
	}

}
