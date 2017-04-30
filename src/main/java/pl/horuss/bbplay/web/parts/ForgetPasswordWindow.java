package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.model.User;
import pl.horuss.bbplay.web.services.UserService;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ForgetPasswordWindow extends Window {

	private static final long serialVersionUID = -5189811432322648592L;

	private UserService userService;

	private Label description;
	private TextField login;

	public ForgetPasswordWindow(UserService userService) {
		super(" " + I18n.t("forgetPassword"));
		this.userService = userService;
		setIcon(FontAwesome.QUESTION_CIRCLE);
		setWidth("500px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent());
	}

	private VerticalLayout windowContent() {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);

		final FormLayout content = new FormLayout();

		description = new Label(I18n.t("forgetPassword.description"), ContentMode.HTML);
		login = new TextField(I18n.t("username") + "/" + I18n.t("email"));
		login.setWidth("100%");
		login.setRequired(true);

		content.addComponent(description);
		content.addComponent(login);

		root.addComponent(content);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.setSpacing(true);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		Label footerText = new Label();

		Button ok = new Button(I18n.t("ok"));
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(event -> {
			if (login.isValid()) {
				User user = userService.sendPasswordReset(login.getValue().trim());
				if (user != null) {
					ForgetPasswordWindow.this.close();
					BBPlay.info(I18n.t("forgetPassword.sent"));
				} else {
					BBPlay.error(I18n.t("forgetPassword.notExist"));
				}
			}
		});

		Button cancel = new Button(I18n.t("cancel"));
		cancel.addClickListener(event -> ForgetPasswordWindow.this.close());

		footer.addComponents(footerText, ok, cancel);
		footer.setExpandRatio(footerText, 1);

		root.addComponent(footer);

		return root;
	}

}
