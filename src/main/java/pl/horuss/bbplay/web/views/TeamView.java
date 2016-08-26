package pl.horuss.bbplay.web.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.model.Player;
import pl.horuss.bbplay.web.services.PlayerService;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

@SpringView(name = "team")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Team", order = 1)
@FontAwesomeIcon(FontAwesome.LIST)
public class TeamView extends VerticalLayout implements View {

	private static final long serialVersionUID = 6747646733790911512L;

	private final PlayerService playerService;

	@Autowired
	public TeamView(PlayerService playerService) {
		this.playerService = playerService;

		setSpacing(true);
		setMargin(true);
		setHeight("100%");

		BeanItemContainer<Player> container = new BeanItemContainer<Player>(Player.class,
				this.playerService.getPlayers());
		GeneratedPropertyContainer wrapperContainer = new GeneratedPropertyContainer(container);
		wrapperContainer.removeContainerProperty("id");
		wrapperContainer.removeContainerProperty("user");

		Grid grid = new Grid();
		grid.setContainerDataSource(wrapperContainer);
		grid.setWidth("100%");
		grid.setHeight("100%");
		grid.setSelectionMode(SelectionMode.SINGLE);

		wrapperContainer.addGeneratedProperty("expander", new PropertyValueGenerator<String>() {
			private static final long serialVersionUID = 2083588858764879470L;

			@Override
			public String getValue(Item item, Object itemId, Object propertyId) {
				if (grid.isDetailsVisible(itemId)) {
					return "-";
				} else {
					return "+";
				}
			}

			@Override
			public Class<String> getType() {
				return String.class;
			}
		});
		Column column = grid.getColumn("expander");
		column.setRenderer(new ButtonRenderer(event -> {
			Object itemId = event.getItemId();
			grid.setDetailsVisible(itemId, !grid.isDetailsVisible(itemId));
		}));
		column.setHeaderCaption("");
		column.setResizable(false);
		column.setSortable(false);
		column.setWidth(60);

		column = grid.getColumn("number");
		column.setWidth(100);

		column = grid.getColumn("position");
		column.setWidth(125);

		column = grid.getColumn("position2");
		column.setHeaderCaption("2nd position");
		column.setWidth(125);

		grid.setDetailsGenerator(new DetailsGenerator() {
			private static final long serialVersionUID = -4798398969385281616L;

			@Override
			public Component getDetails(RowReference rowReference) {
				final Player bean = (Player) rowReference.getItemId();
				// TODO fill with interesting data...
				Label label = new Label("Extra data for " + bean.getFirstName());
				VerticalLayout layout = new VerticalLayout();
				layout.setSpacing(true);
				layout.setMargin(true);
				layout.addComponent(label);
				return layout;
			}
		});

		grid.setColumnOrder("expander", "number");

		addComponent(grid);

	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
