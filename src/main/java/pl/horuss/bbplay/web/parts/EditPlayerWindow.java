package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.model.Player;
import pl.horuss.bbplay.web.services.PlayerService;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
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

	public EditPlayerWindow(PlayerService playerService, Player player, boolean admin) {
		super(" " + I18n.t("player.edit"));
		this.playerService = playerService;
		setIcon(FontAwesome.WRENCH);
		setWidth("650px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent(player, admin));
	}

	private VerticalLayout windowContent(Player player, boolean admin) {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		
		HorizontalLayout formsContainer = new HorizontalLayout();
		formsContainer.setSpacing(true);

		FieldGroup fieldGroup = new BeanFieldGroup<Player>(Player.class);
		fieldGroup.setItemDataSource(new BeanItem<Player>(player));
		
		final FormLayout content1 = new FormLayout();
		Field<?> buildAndBind = fieldGroup.buildAndBind(I18n.t("player.number"), "number");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		buildAndBind = fieldGroup.buildAndBind(I18n.t("player.firstName"), "firstName");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		buildAndBind = fieldGroup.buildAndBind(I18n.t("player.lastName"), "lastName");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		buildAndBind = fieldGroup.buildAndBind(I18n.t("player.position"), "position");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		buildAndBind = fieldGroup.buildAndBind(I18n.t("player.position2"), "position2");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		buildAndBind = fieldGroup.buildAndBind(I18n.t("player.role"), "role");
		buildAndBind.setEnabled(admin);
		content1.addComponent(buildAndBind);
		formsContainer.addComponent(content1);
		
		final FormLayout content2 = new FormLayout();
		content2.addComponent(fieldGroup.buildAndBind(I18n.t("player.birthdate"), "birthdate"));
		content2.addComponent(fieldGroup.buildAndBind(I18n.t("player.height"), "height"));
		content2.addComponent(fieldGroup.buildAndBind(I18n.t("player.email"), "email"));
		content2.addComponent(fieldGroup.buildAndBind(I18n.t("player.phone"), "phone"));
		content2.addComponent(fieldGroup.buildAndBind(I18n.t("player.comment"), "comment"));
		formsContainer.addComponent(content2);
		
		root.addComponent(formsContainer);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.setSpacing(true);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		Label footerText = new Label();

		Button ok = new Button(I18n.t("ok"));
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(event -> {
			try {
				fieldGroup.commit();
				this.savedModel = playerService.save(player);
				EditPlayerWindow.this.close();
				BBPlay.info(I18n.t("saveOk"));
			} catch (CommitException e) {
				BBPlay.error(I18n.t("saveFail"));
			}

		});

		Button cancel = new Button(I18n.t("cancel"));
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
