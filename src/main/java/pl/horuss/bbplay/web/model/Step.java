package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

public class Step {

	private int order;
	private String desc;
	private List<StepEntity> entites;

	public Step(int order, String desc, List<StepEntity> entites) {
		this.order = order;
		this.desc = desc;
		this.entites = entites;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<StepEntity> getEntites() {
		return entites != null ? entites : (entites = new ArrayList<>());
	}

	public void setEntites(List<StepEntity> entites) {
		this.entites = entites;
	}

}
