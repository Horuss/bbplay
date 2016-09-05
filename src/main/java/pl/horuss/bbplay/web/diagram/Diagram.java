package pl.horuss.bbplay.web.diagram;

import org.vaadin.jouni.animator.shared.AnimType;

import pl.horuss.bbplay.web.MainUI;
import pl.horuss.bbplay.web.json.AnnotationExclusionStrategy;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.views.PlaybookEditView;
import pl.horuss.bbplay.web.views.PlaybookView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({ "d3.v4.min.4.2.2.js", "diagram_connector.js" })
public class Diagram extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 4053617012919018688L;
	
	private Play updatedPlay;

	private final Gson gson = new GsonBuilder().setExclusionStrategies(
			new AnnotationExclusionStrategy()).create();

	public Diagram(PlaybookView view) {
		addFunction("updateState", arguments -> {
			String param = arguments.getString(0);
			int step = (int) Math.round(arguments.getNumber(1));
			String desc = arguments.getString(2);

			if (param.equals("end")) {
				view.enable();
			}

			view.getStepDesc().setValue("<strong>Step " + step + ":</strong><br/>" + desc);
			MainUI.animator().animate(view.getStepDesc(), AnimType.FADE_IN).setDuration(400);
			view.getStepsSlider().setValue((double) step);

		});

	}

	public Diagram(PlaybookEditView playbookEditView) {
		addFunction("updateState", arguments -> {
		});

		addFunction("updatePlay", arguments -> {
			this.updatedPlay = gson.fromJson(arguments.getObject(0).toJson(), Play.class);;
		});
	}

	public void init(String data, boolean edit) {
		callFunction("init", data, edit);
	}

	public void draw(int step) {
		callFunction("draw", step);
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
	
	public Play getUpdatedPlay() {
		return updatedPlay;
	}
}
