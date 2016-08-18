package pl.horuss.bbplay.web.d3;

import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.jouni.animator.shared.AnimType;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.Label;

@JavaScript({ "https://d3js.org/d3.v4.min.js", "diagram_connector.js" })
public class Diagram extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 4053617012919018688L;

	public Diagram(AnimatorProxy animator, Label stepDesc) {
		addFunction(
				"updateState",
				arguments -> {
					animator.animate(stepDesc, AnimType.FADE_OUT).setDuration(500).setDelay(100);
					animator.animate(stepDesc, AnimType.FADE_IN).setDuration(500).setDelay(600);
					stepDesc.setValue("Step " + +Math.round(arguments.getNumber(0)) + ": "
							+ arguments.getString(1));
				});
	}

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
