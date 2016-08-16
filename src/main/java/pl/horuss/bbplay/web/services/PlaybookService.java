package pl.horuss.bbplay.web.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.model.PlayType;
import pl.horuss.bbplay.web.model.Step;
import pl.horuss.bbplay.web.model.StepEntity;
import pl.horuss.bbplay.web.model.StepEntityType;

@Service
public class PlaybookService {

	public List<Play> getPlays() {
		// TODO Stub data
		return Arrays.asList(
				new Play("zagr1", "zagr1-opis", PlayType.OFFENSE, Arrays.asList(
						new Step(1, Arrays.asList(new StepEntity(1, 500, 20,
								StepEntityType.PLAYER_1, "L1"), new StepEntity(2, 50, 100,
								StepEntityType.BALL, "B"))),
						new Step(2, Arrays.asList(new StepEntity(1, 200, 100,
								StepEntityType.PLAYER_1, "L1"), new StepEntity(2, 20, 20,
								StepEntityType.BALL, "B"))))),
				new Play("zagr2", "zagr2-opis", PlayType.OFFENSE, Arrays.asList(new Step(1, Arrays
						.asList(new StepEntity(1, 50, 100, StepEntityType.PLAYER_1, "L1"))))));
	}

}
