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
@SideBarItem(sectionId = Sections.VIEWS, caption = "Playbook")
@FontAwesomeIcon(FontAwesome.PLAY_CIRCLE)
public class PlaybookView extends VerticalLayout implements View {

	private static final long serialVersionUID = -808608026129875870L;

	private final PlaybookService playbookService;

	private final Label stepDesc = new Label();
	private Slider delay = new Slider("Step delay (s)");
	private Slider duration = new Slider("Step duration (s)");
	private Button play = new Button("Play");
	private Button reset = new Button("Reset");

	@Autowired
	public PlaybookView(PlaybookService playbookService) {
		this.playbookService = playbookService;

		Diagram diagram = new Diagram(this);

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

		play.addClickListener(event -> {
			disable();
			diagram.play((int) Math.round(duration.getValue()), (int) Math.round(delay.getValue()));
		});

		reset.addClickListener(event -> {
			enable();
			diagram.reset();
		});

		delay.setImmediate(true);
		delay.setMin(1);
		delay.setMax(5);
		delay.setValue(2.0);
		delay.setResolution(0);

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
		right.addComponent(diagram);
		diagram.addStyleName("diagram");

		main.addComponent(right);

		addComponent(main);

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

	public Label getStepDesc() {
		return stepDesc;
	}

	public void enable() {
		play.setEnabled(true);
		duration.setEnabled(true);
		delay.setEnabled(true);
	}

	public void disable() {
		play.setEnabled(false);
		duration.setEnabled(false);
		delay.setEnabled(false);
	}

}
