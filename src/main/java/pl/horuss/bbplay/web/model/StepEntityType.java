package pl.horuss.bbplay.web.model;

public enum StepEntityType {

	BALL(0), PLAYER_1(1), PLAYER_2(2);

	private int id;

	private StepEntityType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
