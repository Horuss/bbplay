package pl.horuss.bbplay.web.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.dao.PlayDao;
import pl.horuss.bbplay.web.model.Play;

@Service
public class PlaybookService {

	@Autowired
	private PlayDao playDao;

	public List<Play> getPlays() {
		return StreamSupport.stream(playDao.findAll().spliterator(), false).collect(
				Collectors.toList());
	}

}
