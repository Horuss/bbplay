package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.model.Play;
import pl.horuss.bbplay.web.services.PlaybookService;

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
		super(" " + "Edit Play");
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
		content.addComponent(fieldGroup.buildAndBind("Name", "name"));
		content.addComponent(fieldGroup.buildAndBind("Call", "call"));
		content.addComponent(fieldGroup.buildAndBind("Description", "desc"));
		Field<?> typeField = fieldGroup.buildAndBind("Type", "type");
		typeField.setRequired(true);
		content.addComponent(typeField);

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
				this.savedModel = playbookService.save(play);
				EditPlayWindow.this.close();
				BBPlay.info("Successfully changed!");
			} catch (CommitException e) {
				BBPlay.error("Failed to save changes");
			}

		});

		Button cancel = new Button("Cancel");
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
