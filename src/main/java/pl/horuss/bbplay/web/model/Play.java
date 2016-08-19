package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

public class Play {

	private String name;
	private String call;
	private String desc;
	private PlayType type;
	private List<Step> steps;

	public Play(String name, String call, String desc, PlayType type, List<Step> steps) {
		this.name = name;
		this.call = call;
		this.desc = desc;
		this.type = type;
		this.steps = steps;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCall() {
		return call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public PlayType getType() {
		return type;
	}

	public void setType(PlayType type) {
		this.type = type;
	}

	public List<Step> getSteps() {
		return steps != null ? steps : (steps = new ArrayList<>());
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
