package pl.horuss.bbplay.web.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.model.Player;
import pl.horuss.bbplay.web.parts.ConfirmWindow;
import pl.horuss.bbplay.web.parts.EditPlayerWindow;
import pl.horuss.bbplay.web.services.PlayerService;
import pl.horuss.bbplay.web.utils.SecurityUtil;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.DetailsGenerator;
import com.vaadin.ui.Grid.RowReference;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "team")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Team", order = 1)
@FontAwesomeIcon(FontAwesome.LIST)
public class TeamView extends VerticalLayout implements View {

	private static final long serialVersionUID = 6747646733790911512L;

	private final PlayerService playerService;

	private Grid grid;

	@SuppressWarnings("serial")
	@Autowired
	public TeamView(PlayerService playerService) {
		this.playerService = playerService;

		setSpacing(true);
		setMargin(true);
		setHeight("100%");

		BeanItemContainer<Player> container = new BeanItemContainer<Player>(Player.class,
				this.playerService.getPlayers());
		GeneratedPropertyContainer wrapperContainer = new GeneratedPropertyContainer(container);
		wrapperContainer.removeContainerProperty("user");
		wrapperContainer.removeContainerProperty("comment");

		grid = new Grid();
		grid.setContainerDataSource(wrapperContainer);
		grid.setWidth("100%");
		grid.setHeight("100%");
		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.addItemClickListener(event -> {
			if (event.isDoubleClick()) {
				Object itemId = event.getItemId();
				grid.setDetailsVisible(itemId, !grid.isDetailsVisible(itemId));
			}
		});

		wrapperContainer.addGeneratedProperty("expander", new PropertyValueGenerator<String>() {

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
			@Override
			public Component getDetails(RowReference rowReference) {
				final Player bean = (Player) rowReference.getItemId();
				// TODO fill with interesting data...
				VerticalLayout layout = new VerticalLayout();
				layout.setSpacing(true);
				layout.setMargin(true);
				Label label = new Label("Comment: " + bean.getComment());
				layout.addComponent(label);
				if (SecurityUtil.isAdmin()) {
					HorizontalLayout buttons = new HorizontalLayout();
					buttons.setSpacing(true);
					Button edit = new Button("Edit");
					edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
					edit.addClickListener(event -> {
						EditPlayerWindow editPlayerWindow = new EditPlayerWindow(playerService,
								bean);
						editPlayerWindow.addCloseListener(e -> {
							if (editPlayerWindow.getSavedModel() != null) {
								refreshGrid();
							}
						});
						UI.getCurrent().addWindow(editPlayerWindow);
					});
					Button remove = new Button("Remove");
					remove.addClickListener(event -> {
						ConfirmWindow.show("Confirm", null, "Are you sure?", result -> {
							if (result) {
								playerService.delete(bean);
								container.removeItem(bean);
								refreshGrid();
							}
						});
					});
					buttons.addComponent(edit);
					buttons.addComponent(remove);
					layout.addComponent(buttons);
				}
				return layout;
			}
		});

		grid.setColumnOrder("expander", "number");

		addComponent(grid);
		setExpandRatio(grid, 1);

		if (SecurityUtil.isAdmin()) {
			Button add = new Button("Add");
			add.addStyleName(ValoTheme.BUTTON_PRIMARY);
			add.addClickListener(event -> {
				EditPlayerWindow editPlayerWindow = new EditPlayerWindow(playerService,
						new Player());
				editPlayerWindow.addCloseListener(e -> {
					if (editPlayerWindow.getSavedModel() != null) {
						container.addItem(editPlayerWindow.getSavedModel());
						refreshGrid();
					}
				});
				UI.getCurrent().addWindow(editPlayerWindow);
			});
			addComponent(add);
		}

	}

	public void refreshGrid() {
		grid.clearSortOrder();
	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
