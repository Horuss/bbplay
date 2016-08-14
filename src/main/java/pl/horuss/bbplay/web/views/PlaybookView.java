package pl.horuss.bbplay.web.views;

import java.util.Collection;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
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
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Slider;
import com.vaadin.ui.VerticalLayout;

@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "playbook")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Playbook")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class PlaybookView extends VerticalLayout implements View {

	private static final long serialVersionUID = -808608026129875870L;

	private final PlaybookService playbookService;

	private final Diagram diagram = new Diagram();
	private Slider delay = new Slider("Delay");
	private Slider duration = new Slider("Duration");
	private Button play = new Button("Play");

	@Autowired
	public PlaybookView(PlaybookService playbookService) {
		this.playbookService = playbookService;

		HorizontalLayout main = new HorizontalLayout();

		VerticalLayout left = new VerticalLayout();

		VerticalLayout right = new VerticalLayout();
		right.setVisible(false);

		BeanItemContainer<Play> container = new BeanItemContainer<Play>(Play.class,
				this.playbookService.getPlays());

		Grid grid = new Grid(container);
		grid.setColumns("name", "desc");
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(event -> {
			Collection<Object> selectedRows = grid.getSelectionModel().getSelectedRows();
			if (selectedRows != null && !selectedRows.isEmpty()) {
				diagram.reset();
				right.setVisible(true);
			} else {
				right.setVisible(false);
			}
		});

		left.addComponent(grid);

		main.addComponent(left);

		play.addClickListener(event -> {
			JsonConfig jsonConfig = new JsonConfig();
			JSONArray jsonNodes = (JSONArray) JSONSerializer.toJSON(((Play) grid
					.getSelectionModel().getSelectedRows().toArray()[0]).getSteps(), jsonConfig);
			diagram.play(jsonNodes.toString(), (int) Math.round(duration.getValue()),
					(int) Math.round(delay.getValue()));
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

		right.addComponent(delay);
		right.addComponent(duration);
		right.addComponent(play);
		right.addComponent(diagram);

		main.addComponent(right);

		addComponent(main);

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
