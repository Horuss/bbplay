package pl.horuss.bbplay.web.views;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.model.Player;
import pl.horuss.bbplay.web.parts.ConfirmWindow;
import pl.horuss.bbplay.web.parts.EditPlayerWindow;
import pl.horuss.bbplay.web.services.PlayerService;
import pl.horuss.bbplay.web.utils.SecurityUtil;

import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "team")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Team", order = 1)
@FontAwesomeIcon(FontAwesome.LIST)
public class TeamView extends VerticalLayout implements View {

	private static final long serialVersionUID = 6747646733790911512L;

	private final PlayerService playerService;

	private Grid grid;

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
		wrapperContainer.removeContainerProperty("birthdate");
		wrapperContainer.removeContainerProperty("phone");
		wrapperContainer.removeContainerProperty("email");
		wrapperContainer.removeContainerProperty("height");

		grid = new Grid();
		grid.setContainerDataSource(wrapperContainer);
		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.addSelectionListener(event -> {
			event.getAdded().forEach((item -> {
				grid.setDetailsVisible(item, true);
			}));
			event.getRemoved().forEach((item -> {
				grid.setDetailsVisible(item, false);
			}));
		});
		
		grid.setRowStyleGenerator(rowRef -> {
			Player player = (Player) rowRef.getItemId();
			if (BBPlay.currentUser().compareTo(player.getUser()) == 0) {
				return "bold";
			}
			return null;
		});

		Column column = grid.getColumn("number");
		column.setWidth(110);

		column = grid.getColumn("position");
		column.setWidth(135);

		column = grid.getColumn("position2");
		column.setHeaderCaption("2nd position");
		column.setWidth(135);

		grid.setDetailsGenerator(rowReference -> {
			final Player bean = (Player) rowReference.getItemId();
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);
			if (bean.getBirthdate() != null) {
				long yearsDelta = Period.between(
						Instant.ofEpochMilli(bean.getBirthdate().getTime())
								.atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now())
						.getYears();
				layout.addComponent(new Label("<strong>Age:</strong> " + yearsDelta,
						ContentMode.HTML));
			}
			if (bean.getHeight() != null && !bean.getHeight().equals(0)) {
				layout.addComponent(new Label("<strong>Height:</strong> " + bean.getHeight()
						+ " cm", ContentMode.HTML));
			}
			StringBuilder sb = new StringBuilder();
			if (bean.getEmail() != null && !bean.getEmail().isEmpty()) {
				sb.append(bean.getEmail());
			}
			if (bean.getPhone() != null && !bean.getPhone().isEmpty()) {
				if (!sb.toString().isEmpty()) {
					sb.append(", ");
				}
				sb.append(bean.getPhone());
			}
			if (!sb.toString().isEmpty()) {
				layout.addComponent(new Label("<strong>Contact:</strong> " + sb.toString(),
						ContentMode.HTML));
			}
			if (bean.getComment() != null && !bean.getComment().isEmpty()) {
				layout.addComponent(new Label("<strong>Comment:</strong> " + bean.getComment(),
						ContentMode.HTML));
			}
			HorizontalLayout buttons = new HorizontalLayout();
			buttons.setSpacing(true);
			if (SecurityUtil.isAdmin() || BBPlay.currentUser().compareTo(bean.getUser()) == 0) {
				Button edit = new Button("Edit");
				edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
				edit.addClickListener(event1 -> {
					EditPlayerWindow editPlayerWindow = new EditPlayerWindow(playerService, bean, SecurityUtil.isAdmin());
					editPlayerWindow.addCloseListener(e -> {
						if (editPlayerWindow.getSavedModel() != null) {
							refreshGrid();
						}
					});
					UI.getCurrent().addWindow(editPlayerWindow);
				});
				buttons.addComponent(edit);
			}
			if (SecurityUtil.isAdmin()) {
				Button remove = new Button("Remove");
				remove.addClickListener(event2 -> {
					ConfirmWindow.show("Confirm", null, "Are you sure?", result -> {
						if (result) {
							playerService.delete(bean);
							container.removeItem(bean);
							refreshGrid();
						}
					});
				});
				buttons.addComponent(remove);
			}
			if (buttons.getComponentCount() > 0) {
				layout.addComponent(buttons);
			}
			return layout;
		});

		grid.setColumnOrder("number");
		grid.setSortOrder(Arrays.asList(new SortOrder("number", SortDirection.ASCENDING)));
		grid.setWidth("100%");
		grid.setHeight("100%");

		addComponent(grid);
		setExpandRatio(grid, 1);

		if (SecurityUtil.isAdmin()) {
			Button add = new Button("Add");
			add.addStyleName(ValoTheme.BUTTON_PRIMARY);
			add.addClickListener(event -> {
				EditPlayerWindow editPlayerWindow = new EditPlayerWindow(playerService,
						new Player(), true);
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
		for (Object item : grid.getContainerDataSource().getItemIds()) {
			grid.setDetailsVisible(item, false);
		}
		grid.select(null);
	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
