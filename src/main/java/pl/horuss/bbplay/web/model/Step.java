package pl.horuss.bbplay.web.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import pl.horuss.bbplay.web.json.JsonExclude;

@Entity
public class Step implements Comparable<Step> {

	@Id
	@GeneratedValue
	@Column(name = "st_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "st_pl_id", foreignKey = @ForeignKey(name = "fk_st_pl"))
	@JsonExclude
	private Play play;

	@Column(name = "st_order")
	private int order;

	@Column(name = "st_desc")
	private String desc;

	@OneToMany(mappedBy = "step", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StepEntity> entities;

	public Step() {
		this.desc = "";
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

	public List<StepEntity> getEntities() {
		return entities != null ? entities : (entities = new ArrayList<>());
	}

	public void setEntities(List<StepEntity> entities) {
		this.entities = entities;
	}

	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	@Override
	public int compareTo(Step o) {
		return new Integer(this.order).compareTo(new Integer(o.order));
	}

}
