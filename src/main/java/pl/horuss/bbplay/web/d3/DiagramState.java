package pl.horuss.bbplay.web.d3;

import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;

public class DiagramState extends JavaScriptComponentState {

	private static final long serialVersionUID = -4411532192058430987L;

	private List<Integer> coords;

	public List<Integer> getCoords() {
		return coords;
	}

	public void setCoords(final List<Integer> coords) {
		this.coords = coords;
	}
}
