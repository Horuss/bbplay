package pl.horuss.bbplay.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(StepEntityId.class)
public class StepEntity {

	@Id
	@GeneratedValue
	@Column(name = "se_id")
	private long id;

	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "se_st_id")
	private Step step;

	@Column(name = "se_x")
	private int x;

	@Column(name = "se_y")
	private int y;

	@Column(name = "se_type")
	private StepEntityType type;

	@Column(name = "se_label")
	private String label;

	public StepEntity() {

	}

	public long getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public StepEntityType getType() {
		return type;
	}

	public void setType(StepEntityType type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
