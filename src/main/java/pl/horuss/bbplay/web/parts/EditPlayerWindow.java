package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.model.Player;
import pl.horuss.bbplay.web.services.PlayerService;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class EditPlayerWindow extends Window {

	private static final long serialVersionUID = 5857882394383835558L;

	private PlayerService playerService;

	private Player savedModel = null;

	public EditPlayerWindow(PlayerService playerService, Player player) {
		super(" " + "Edit Player");
		this.playerService = playerService;
		setIcon(FontAwesome.WRENCH);
		setWidth("350px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent(player));
	}

	private VerticalLayout windowContent(Player player) {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);

		final FormLayout content = new FormLayout();

		FieldGroup fieldGroup = new BeanFieldGroup<Player>(Player.class);
		fieldGroup.setItemDataSource(new BeanItem<Player>(player));
		content.addComponent(fieldGroup.buildAndBind("Number", "number"));
		content.addComponent(fieldGroup.buildAndBind("First name", "firstName"));
		content.addComponent(fieldGroup.buildAndBind("Last name", "lastName"));
		content.addComponent(fieldGroup.buildAndBind("Position", "position"));
		content.addComponent(fieldGroup.buildAndBind("2nd position", "position2"));
		content.addComponent(fieldGroup.buildAndBind("Role", "role"));
		content.addComponent(fieldGroup.buildAndBind("Comment", "comment"));

		root.addComponent(content);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.setSpacing(true);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		Label footerText = new Label();

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(event -> {
			try {
				fieldGroup.commit();
				this.savedModel = playerService.save(player);
				EditPlayerWindow.this.close();
				BBPlay.info("Successfully changed!");
			} catch (CommitException e) {
				BBPlay.error("Failed to save changes");
			}

		});

		Button cancel = new Button("Cancel");
		cancel.addClickListener(event -> EditPlayerWindow.this.close());

		footer.addComponents(footerText, ok, cancel);
		footer.setExpandRatio(footerText, 1);

		root.addComponent(footer);

		return root;
	}

	public Player getSavedModel() {
		return savedModel;
	}

}
