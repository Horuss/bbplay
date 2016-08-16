package pl.horuss.bbplay.web.model;

public enum PlayType {

	OFFENSE(0);

	private int id;

	private PlayType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
