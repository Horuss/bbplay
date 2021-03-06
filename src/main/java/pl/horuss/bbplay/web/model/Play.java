package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Play {

	@Id
	@GeneratedValue
	@Column(name = "pl_id")
	private Long id;

	@Column(name = "pl_name")
	private String name;

	@Column(name = "pl_call")
	private String call;

	@Column(name = "pl_desc")
	private String desc;

	@Column(name = "pl_type")
	private PlayType type;
	
	@Column(name = "pl_published")
	private boolean published;

	@OneToMany(mappedBy = "play", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Step> steps;

	public Play() {
		this.name = "";
		this.call = "";
		this.desc = "";
	}

	public boolean isPersist() {
		return id != null;
	}

	public Long getId() {
		return id;
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

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public List<Step> getSteps() {
		return steps != null ? steps : (steps = new ArrayList<>());
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
