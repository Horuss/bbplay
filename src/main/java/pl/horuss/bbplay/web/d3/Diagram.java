package pl.horuss.bbplay.web.d3;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({ "https://d3js.org/d3.v4.min.js", "diagram_connector.js" })
public class Diagram extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 4053617012919018688L;
	
	public void init(String data) {
		callFunction("init", data);
	}

	public void play(int speed, int delay) {
		callFunction("play", speed, delay);
	}

	public void reset() {
		callFunction("reset");
	}

	@Override
	public DiagramState getState() {
		return (DiagramState) super.getState();
	}
}
