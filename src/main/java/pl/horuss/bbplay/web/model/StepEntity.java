package pl.horuss.bbplay.web.model;

public class StepEntity {

	private int id;
	private int x;
	private int y;
	private String label;

	public StepEntity(int id, int x, int y, String label) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.setLabel(label);
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
