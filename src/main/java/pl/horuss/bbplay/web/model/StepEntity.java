package pl.horuss.bbplay.web.model;

public class StepEntity {

	private int id;
	private int x;
	private int y;
	private StepEntityType type;
	private String label;

	public StepEntity(int id, int x, int y, StepEntityType type, String label) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.type = type;
		this.label = label;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
