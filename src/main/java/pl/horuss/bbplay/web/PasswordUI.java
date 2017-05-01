package pl.horuss.bbplay.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import pl.horuss.bbplay.web.model.User;
import pl.horuss.bbplay.web.services.UserService;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.annotations.Theme;
import com.vaadin.data.validator.AbstractStringValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/password")
@Theme("bbplay-theme")
public class PasswordUI extends UI {

	private static final long serialVersionUID = -5852012640350274988L;

	@Autowired
	I18N i18n;

	@Autowired
	private UserService userService;

	private PasswordField passwordField;
	private PasswordField passwordFieldRepeat;

	private Button reset;

	@SuppressWarnings("serial")
	@Override
	protected void init(VaadinRequest request) {

		getUI().setLocale(BBPlay.getLanguage(request.getLocale().getLanguage()));
		I18n.init(i18n);
		getPage().setTitle("BBPlay");

		String token = request.getParameter("token");
		if (token == null || token.isEmpty()) {
			BBPlay.error(I18n.t("noToken"));
			return;
		}
		User user = userService.checkToken(token);
		if (user == null) {
			BBPlay.error(I18n.t("wrongPasswordToken"));
			return;
		}

		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();

		passwordField = new PasswordField(I18n.t("changePassword.new"));
		passwordFieldRepeat = new PasswordField(I18n.t("changePassword.confirm"));
		passwordFieldRepeat.setImmediate(false);
		passwordFieldRepeat.addValidator(new AbstractStringValidator(I18n
				.t("changePassword.errorMatch")) {
			@Override
			protected boolean isValidValue(String value) {
				return value.equals(passwordField.getValue());
			}
		});
		reset = new Button(I18n.t("reset"));

		loginForm.addComponent(passwordField);
		loginForm.addComponent(passwordFieldRepeat);
		loginForm.addComponent(reset);

		reset.addStyleName(ValoTheme.BUTTON_PRIMARY);
		reset.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		reset.addClickListener(event -> reset(user));

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSpacing(true);
		loginLayout.setSizeUndefined();

		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		VerticalLayout rootLayout = new VerticalLayout(loginLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		setContent(rootLayout);
		setSizeFull();

	}

	private void reset(User user) {
		reset.setEnabled(false);
		if (passwordFieldRepeat.isValid()) {
			String msg = userService.resetPassword(user, passwordFieldRepeat.getValue());
			if (msg != null) {
				BBPlay.error(msg);
			} else {
				getUI().getPage().setLocation("/login?changed");
			}
		}
		reset.setEnabled(true);
	}
}
