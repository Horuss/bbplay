package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

public class Play {

	private String name;
	private String desc;
	private List<Step> steps;

	public Play(String name, String desc, List<Step> steps) {
		this.name = name;
		this.desc = desc;
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<Step> getSteps() {
		return steps != null ? steps : (steps = new ArrayList<>());
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
