package pl.horuss.bbplay.web.views;

import java.util.Collection;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.d3.Diagram;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.services.PlaybookService;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

// @Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "playbook")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Playbook", order = 2)
@FontAwesomeIcon(FontAwesome.PLAY_CIRCLE)
public class PlaybookView extends VerticalLayout implements View {

	private static final long serialVersionUID = -808608026129875870L;

	private final PlaybookService playbookService;

	private Diagram diagram;
	private Label stepDesc;
	private Slider stepsSlider;
	private HorizontalLayout stepsSliderLayout;
	private Slider delay;
	private Slider duration;
	private Button play;
	private Button reset;

	@Autowired
	public PlaybookView(PlaybookService playbookService) {
		this.playbookService = playbookService;

		setSpacing(true);
		setMargin(true);

		HorizontalLayout main = new HorizontalLayout();

		VerticalLayout left = new VerticalLayout();
		left.setWidth("250px");
		left.setMargin(true);
		left.setSpacing(true);

		VerticalLayout right = new VerticalLayout();
		right.setMargin(true);
		right.setVisible(false);

		VerticalLayout bottom = new VerticalLayout();
		bottom.setVisible(false);
		final Label description = new Label();
		description.setContentMode(ContentMode.HTML);
		bottom.addComponent(description);
		bottom.addComponent(new Label("<hr />", ContentMode.HTML));
		stepsSliderLayout = new HorizontalLayout();
		stepsSliderLayout.setSpacing(true);
		stepsSlider = new Slider();
		stepsSlider.setMin(1);
		stepsSlider.setMax(1);
		stepsSlider.setValue(1.0);
		stepsSlider.setResolution(0);
		stepsSlider.setWidth("100%");
		stepsSlider.addValueChangeListener(event -> {
			diagram.draw(((Double) stepsSlider.getValue()).intValue() - 1);
		});
		Button prevStep = new Button(FontAwesome.ARROW_LEFT);
		prevStep.addClickListener(event -> {
			if (((Double) stepsSlider.getValue()).intValue() != ((Double) stepsSlider.getMin())
					.intValue()) {
				stepsSlider.setValue(stepsSlider.getValue() - 1);
			}
		});
		Button nextStep = new Button(FontAwesome.ARROW_RIGHT);
		nextStep.addClickListener(event -> {
			if (((Double) stepsSlider.getValue()).intValue() != ((Double) stepsSlider.getMax())
					.intValue()) {
				stepsSlider.setValue(stepsSlider.getValue() + 1);
			}
		});
		stepsSliderLayout.addComponent(prevStep);
		stepsSliderLayout.addComponent(stepsSlider);
		stepsSliderLayout.addComponent(nextStep);
		bottom.addComponent(stepsSliderLayout);
		stepDesc = new Label();
		stepDesc.setContentMode(ContentMode.HTML);
		bottom.addComponent(stepDesc);

		BeanItemContainer<Play> container = new BeanItemContainer<Play>(Play.class,
				this.playbookService.getPlays());

		Grid grid = new Grid(container);
		grid.setWidth("100%");
		grid.setHeight("200px");
		grid.setColumns("name", "call");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(event -> {
			Collection<Object> selectedRows = grid.getSelectionModel().getSelectedRows();
			if (selectedRows != null && !selectedRows.isEmpty()) {
				Play selectedPlay = (Play) grid.getSelectionModel().getSelectedRows().toArray()[0];
				right.setVisible(true);
				bottom.setVisible(true);
				description.setValue(selectedPlay.getDesc());
				stepsSlider.setMax(selectedPlay.getSteps().size());
				JsonConfig jsonConfig = new JsonConfig();
				JSONObject jsonNodes = (JSONObject) JSONSerializer.toJSON(selectedPlay, jsonConfig);
				diagram.init(jsonNodes.toString());
			} else {
				right.setVisible(false);
				bottom.setVisible(false);
			}
		});

		left.addComponent(grid);
		left.addComponent(bottom);

		main.addComponent(left);

		play = new Button();
		play.setIcon(FontAwesome.PLAY);
		play.addClickListener(event -> {
			disable();
			diagram.play((int) Math.round(duration.getValue()), (int) Math.round(delay.getValue()));
		});

		reset = new Button();
		reset.setIcon(FontAwesome.STOP);
		reset.addClickListener(event -> {
			enable();
			diagram.reset();
		});

		delay = new Slider("Step delay (s)");
		delay.setImmediate(true);
		delay.setMin(1);
		delay.setMax(5);
		delay.setValue(2.0);
		delay.setResolution(0);

		duration = new Slider("Step duration (s)");
		duration.setImmediate(true);
		duration.setMin(1);
		duration.setMax(5);
		duration.setValue(2.0);
		duration.setResolution(0);

		HorizontalLayout toolbar = new HorizontalLayout();
		toolbar.setSpacing(true);

		toolbar.addComponent(play);
		toolbar.addComponent(reset);
		toolbar.addComponent(delay);
		toolbar.addComponent(duration);

		right.addComponent(toolbar);
		diagram = new Diagram(this);
		diagram.addStyleName("diagram");
		right.addComponent(diagram);

		main.addComponent(right);

		addComponent(main);

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

	public Label getStepDesc() {
		return stepDesc;
	}

	public Slider getStepsSlider() {
		return stepsSlider;
	}

	public void enable() {
		play.setEnabled(true);
		duration.setEnabled(true);
		delay.setEnabled(true);
		stepsSliderLayout.setEnabled(true);
	}

	public void disable() {
		play.setEnabled(false);
		duration.setEnabled(false);
		delay.setEnabled(false);
		stepsSliderLayout.setEnabled(false);
	}

}
