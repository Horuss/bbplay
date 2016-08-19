package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Step {

	@Id
	@GeneratedValue
	@Column(name = "st_id")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "st_pl_id")
	private Play play;

	@Column(name = "st_order")
	private int order;

	@Column(name = "st_desc")
	private String desc;

	@OneToMany(mappedBy = "step", fetch = FetchType.EAGER)
	private List<StepEntity> entites;

	public Step() {

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
