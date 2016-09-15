package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.services.PlaybookService;
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

public class EditPlayWindow extends Window {

	private static final long serialVersionUID = 5857882394383835558L;

	private PlaybookService playbookService;

	private Play savedModel = null;

	public EditPlayWindow(PlaybookService playbookService, Play play) {
		super(" " + I18n.t("plays.edit"));
		this.playbookService = playbookService;
		setIcon(FontAwesome.WRENCH);
		setWidth("350px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent(play));
	}

	private VerticalLayout windowContent(Play play) {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);

		final FormLayout content = new FormLayout();

		FieldGroup fieldGroup = new BeanFieldGroup<Play>(Play.class);
		fieldGroup.setItemDataSource(new BeanItem<Play>(play));
		content.addComponent(fieldGroup.buildAndBind(I18n.t("plays.name"), "name"));
		content.addComponent(fieldGroup.buildAndBind(I18n.t("plays.call"), "call"));
		content.addComponent(fieldGroup.buildAndBind(I18n.t("plays.description"), "desc"));
		Field<?> typeField = fieldGroup.buildAndBind(I18n.t("plays.type"), "type");
		typeField.setRequired(true);
		content.addComponent(typeField);
		content.addComponent(fieldGroup.buildAndBind(I18n.t("plays.published"), "published"));

		root.addComponent(content);

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
				this.savedModel = playbookService.save(play);
				EditPlayWindow.this.close();
				BBPlay.info(I18n.t("saveOk"));
			} catch (CommitException e) {
				BBPlay.error(I18n.t("saveFail"));
			}

		});

		Button cancel = new Button(I18n.t("cancel"));
		cancel.addClickListener(event -> EditPlayWindow.this.close());

		footer.addComponents(footerText, ok, cancel);
		footer.setExpandRatio(footerText, 1);

		root.addComponent(footer);

		return root;
	}

	public Play getSavedModel() {
		return savedModel;
	}

}
