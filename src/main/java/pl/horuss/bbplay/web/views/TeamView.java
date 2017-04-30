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
import pl.horuss.bbplay.web.utils.I18n;
import pl.horuss.bbplay.web.utils.SecurityUtil;

import com.vaadin.data.sort.SortOrder;
import com.vaadin.data.util.BeanItemContainer;
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
@SideBarItem(sectionId = Sections.VIEWS, captionCode = "team", order = 1)
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

		grid = new Grid(container);
		grid.setColumns("number", "firstName", "lastName", "position", "position2", "role");
		grid.setSelectionMode(SelectionMode.SINGLE);

		grid.setSortOrder(Arrays.asList(new SortOrder("number", SortDirection.ASCENDING)));
		grid.setWidth("100%");
		grid.setHeight("100%");

		Column col = grid.getColumn("firstName");
		col.setHeaderCaption(I18n.t("player.firstName"));
		col = grid.getColumn("lastName");
		col.setHeaderCaption(I18n.t("player.lastName"));
		col = grid.getColumn("number");
		col.setWidth(110);
		col.setHeaderCaption(I18n.t("player.number"));
		col = grid.getColumn("position");
		col.setWidth(135);
		col.setHeaderCaption(I18n.t("player.position"));
		col = grid.getColumn("position2");
		col.setWidth(135);
		col.setHeaderCaption(I18n.t("player.position2"));
		col = grid.getColumn("role");
		col.setHeaderCaption(I18n.t("player.role"));

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
				layout.addComponent(new Label("<strong>" + I18n.t("player.age") + ":</strong> "
						+ yearsDelta, ContentMode.HTML));
			}
			if (bean.getHeight() != null && !bean.getHeight().equals(0)) {
				layout.addComponent(new Label("<strong>" + I18n.t("player.height") + ":</strong> "
						+ bean.getHeight() + " cm", ContentMode.HTML));
			}
			StringBuilder sb = new StringBuilder();
			if (bean.getUser() != null && bean.getUser().getEmail() != null
					&& !bean.getUser().getEmail().isEmpty()) {
				sb.append(bean.getUser().getEmail());
			}
			if (bean.getPhone() != null && !bean.getPhone().isEmpty()) {
				if (!sb.toString().isEmpty()) {
					sb.append(", ");
				}
				sb.append(bean.getPhone());
			}
			if (!sb.toString().isEmpty()) {
				layout.addComponent(new Label("<strong>" + I18n.t("player.contact") + ":</strong> "
						+ sb.toString(), ContentMode.HTML));
			}
			if (bean.getComment() != null && !bean.getComment().isEmpty()) {
				layout.addComponent(new Label("<strong>" + I18n.t("player.comment") + ":</strong> "
						+ bean.getComment(), ContentMode.HTML));
			}
			HorizontalLayout buttons = new HorizontalLayout();
			buttons.setSpacing(true);
			if (SecurityUtil.isAdmin() || BBPlay.currentUser().compareTo(bean.getUser()) == 0) {
				Button edit = new Button(I18n.t("edit"));
				edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
				edit.addClickListener(event1 -> {
					EditPlayerWindow editPlayerWindow = new EditPlayerWindow(playerService, bean,
							SecurityUtil.isAdmin());
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
				Button remove = new Button(I18n.t("remove"));
				remove.addClickListener(event2 -> {
					ConfirmWindow.show(I18n.t("confirm"), null, I18n.t("confirmQuestion"),
							result -> {
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

		addComponent(grid);
		setExpandRatio(grid, 1);

		if (SecurityUtil.isAdmin()) {
			Button add = new Button(I18n.t("add"));
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
