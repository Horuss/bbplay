package pl.horuss.bbplay.web.views;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.diagram.Diagram;
import pl.horuss.bbplay.web.json.AnnotationExclusionStrategy;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.services.PlaybookService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vaadin.data.Property.ValueChangeListener;
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
	private final Gson gson = new GsonBuilder().setExclusionStrategies(
			new AnnotationExclusionStrategy()).create();

	private Diagram diagram;
	private Label stepDesc;
	private Slider stepsSlider;
	private ValueChangeListener stepsSliderListener;
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
		stepsSliderLayout = new HorizontalLayout();
		stepsSliderLayout.setSpacing(true);
		stepsSlider = new Slider();
		stepsSlider.setMin(1);
		stepsSlider.setMax(1);
		stepsSlider.setValue(1.0);
		stepsSlider.setResolution(0);
		stepsSlider.setWidth("100%");
		stepsSliderListener = event -> diagram
				.draw(((Double) stepsSlider.getValue()).intValue() - 1);
		stepsSlider.addValueChangeListener(stepsSliderListener);
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
		grid.setHeight("350px");
		grid.setColumns("name", "call");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(event -> {
			Collection<Object> selectedRows = event.getSelected();
			if (selectedRows != null && !selectedRows.isEmpty()) {
				Play selectedPlay = (Play) selectedRows.toArray()[0];
				right.setVisible(true);
				bottom.setVisible(true);
				stepsSlider.setMax(playbookService.getSteps(selectedPlay).size());
				String jsonSelectedPlay = gson.toJson(selectedPlay);
				diagram.init(jsonSelectedPlay, false);
				diagram.draw(0);
			} else {
				right.setVisible(false);
				bottom.setVisible(false);
			}
			event.getRemoved().forEach((item -> {
				grid.setDetailsVisible(item, false);
			}));
			event.getAdded().forEach((item -> {
				grid.setDetailsVisible(item, true);
			}));
		});
		grid.setDetailsGenerator(rowReference -> {
			final Play bean = (Play) rowReference.getItemId();
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.addComponent(new Label("<strong>Type:</strong> " + bean.getType(),
					ContentMode.HTML));
			layout.addComponent(new Label("<strong>Description:</strong> " + bean.getDesc(),
					ContentMode.HTML));
			return layout;
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
			diagram.draw(0);
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
		stepsSlider.addValueChangeListener(stepsSliderListener);
	}

	public void disable() {
		play.setEnabled(false);
		duration.setEnabled(false);
		delay.setEnabled(false);
		stepsSliderLayout.setEnabled(false);
		stepsSlider.removeValueChangeListener(stepsSliderListener);
	}

}
