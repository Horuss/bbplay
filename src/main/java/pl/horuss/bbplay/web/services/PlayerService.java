package pl.horuss.bbplay.web.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.dao.PlayerDao;
import pl.horuss.bbplay.web.model.Player;

@Service
public class PlayerService {

	@Autowired
	private PlayerDao playerDao;

	public List<Player> getPlayers() {
		return StreamSupport.stream(playerDao.findAll().spliterator(), false).collect(
				Collectors.toList());
	}

}