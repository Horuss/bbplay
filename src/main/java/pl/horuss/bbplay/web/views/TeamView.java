package pl.horuss.bbplay.web.views;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

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

		grid = new Grid();
		grid.setContainerDataSource(wrapperContainer);
		grid.setSelectionMode(SelectionMode.SINGLE);
		
		grid.addSelectionListener(event -> {
			event.getRemoved().forEach((item -> {
				grid.setDetailsVisible(item, false);
			}));
			event.getAdded().forEach((item -> {
				grid.setDetailsVisible(item, true);
			}));
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
			// TODO fill with interesting data...
			VerticalLayout layout = new VerticalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);
			if (bean.getComment() != null && !bean.getComment().isEmpty()) {
				Label label = new Label("<strong>Comment:</strong> " + bean.getComment(),
						ContentMode.HTML);
				layout.addComponent(label);
			}
			layout.addComponent(new Label("... and some other imporant info, photo etc."));
			if (SecurityUtil.isAdmin()) {
				HorizontalLayout buttons = new HorizontalLayout();
				buttons.setSpacing(true);
				Button edit = new Button("Edit");
				edit.addStyleName(ValoTheme.BUTTON_PRIMARY);
				edit.addClickListener(event1 -> {
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
				remove.addClickListener(event2 -> {
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
