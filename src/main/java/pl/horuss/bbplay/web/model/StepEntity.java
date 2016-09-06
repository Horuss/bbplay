package pl.horuss.bbplay.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import pl.horuss.bbplay.web.json.JsonExclude;

@Entity
public class StepEntity {

	@Id
	@GeneratedValue
	@Column(name = "se_id")
	private Long id;

	@Column(name = "se_en_id")
	private long entityId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "se_st_id", foreignKey = @ForeignKey(name = "fk_se_st"))
	@JsonExclude
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
		this.label = "";
	}

	public StepEntity(StepEntity other) {
		this.entityId = other.entityId;
		this.label = other.label;
		this.type = other.type;
		this.x = other.x;
		this.y = other.y;
	}

	public Long getId() {
		return id;
	}

	public long getEntityId() {
		return entityId;
	}

	public void setEntityId(long entityId) {
		this.entityId = entityId;
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

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

}
