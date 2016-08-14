package pl.horuss.bbplay.web.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.model.Step;
import pl.horuss.bbplay.web.model.StepEntity;

@Service
public class PlaybookService {

	public List<Play> getPlays() {
		// TODO Stub data
		return Arrays.asList(
				new Play("zagr1", "zagr1-opis", Arrays.asList(
						new Step(1, Arrays.asList(new StepEntity(1, 10, 20, "L1"), new StepEntity(
								2, 50, 100, "L2"))),
						new Step(2, Arrays.asList(new StepEntity(1, 50, 100, "L1"), new StepEntity(
								2, 20, 20, "L2"))))),
				new Play("zagr2", "zagr2-opis", Arrays.asList(new Step(1, Arrays
						.asList(new StepEntity(1, 50, 100, "L1"))))));
	}

}
