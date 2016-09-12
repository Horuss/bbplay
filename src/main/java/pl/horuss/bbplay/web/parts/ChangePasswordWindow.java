package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.services.UserService;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ChangePasswordWindow extends Window {

	private static final long serialVersionUID = 5043610172992702045L;

	private UserService userService;

	private PasswordField oldPassword;
	private PasswordField newPassword;
	private PasswordField confirmPassword;

	public ChangePasswordWindow(UserService userService) {
		super(" " + I18n.t("changePassword"));
		this.userService = userService;
		setIcon(FontAwesome.WRENCH);
		setWidth("350px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent());
	}

	@SuppressWarnings("serial")
	private VerticalLayout windowContent() {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);

		final FormLayout content = new FormLayout();

		confirmPassword = new PasswordField(I18n.t("changePassword.confirm"));
		confirmPassword.setImmediate(false);
		confirmPassword.addValidator(new AbstractStringValidator(I18n
				.t("changePassword.errorMatch")) {
			@Override
			protected boolean isValidValue(String value) {
				return value.equals(newPassword.getValue());
			}
		});
		content.addComponent(oldPassword = new PasswordField(I18n.t("changePassword.current")));
		content.addComponent(newPassword = new PasswordField(I18n.t("changePassword.new")));
		content.addComponent(confirmPassword);

		root.addComponent(content);

		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.setSpacing(true);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		Label footerText = new Label();

		Button ok = new Button(I18n.t("ok"));
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(event -> {
			if (confirmPassword.isValid()) {
				String changePasswordResult = userService.changePassword(oldPassword.getValue(),
						newPassword.getValue());
				if (changePasswordResult == null) {
					ChangePasswordWindow.this.close();
					BBPlay.info(I18n.t("changePassword.success"));
				} else {
					BBPlay.error(changePasswordResult);
				}
			}
		});

		Button cancel = new Button(I18n.t("cancel"));
		cancel.addClickListener(event -> ChangePasswordWindow.this.close());

		footer.addComponents(footerText, ok, cancel);
		footer.setExpandRatio(footerText, 1);

		root.addComponent(footer);

		return root;
	}

}
