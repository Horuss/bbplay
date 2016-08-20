package pl.horuss.bbplay.web.d3;

import org.vaadin.jouni.animator.shared.AnimType;

import pl.horuss.bbplay.web.MainUI;
import pl.horuss.bbplay.web.views.PlaybookView;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({ "https://d3js.org/d3.v4.min.js", "diagram_connector.js" })
public class Diagram extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 4053617012919018688L;

	public Diagram(PlaybookView view) {
		addFunction("updateState", arguments -> {
			String param = arguments.getString(0);
			int step = (int) Math.round(arguments.getNumber(1));
			String desc = arguments.getString(2);

			if (param.equals("end")) {
				view.enable();
			}

			if (!desc.isEmpty()) {
				view.getStepDesc().setValue("<strong>Step " + step + ":</strong><br/>" + desc);
				if (step != 1) {
					MainUI.animator().animate(view.getStepDesc(), AnimType.FADE_IN)
							.setDuration(400);
				}
			}

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
