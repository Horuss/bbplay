package pl.horuss.bbplay.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

import pl.horuss.bbplay.web.parts.LanguageComboBox;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/login")
@Theme("bbplay-theme")
public class LoginUI extends UI {

	private static final long serialVersionUID = -2647524179548770940L;

	private static final Logger logger = LoggerFactory.getLogger(LoginUI.class);

	@Autowired
	VaadinSharedSecurity vaadinSecurity;

	@Autowired
	I18N i18n;

	private TextField userName;

	private PasswordField passwordField;

	private CheckBox rememberMe;

	private Button login;

	private Label loginFailedLabel;
	private Label loggedOutLabel;

	@Override
	protected void init(VaadinRequest request) {

		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			getUI().getPage().setLocation("/#!");
		}

		getUI().setLocale(BBPlay.getLanguage(request.getLocale().getLanguage()));
		I18n.init(i18n);
		getPage().setTitle("BBPlay");

		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();

		userName = new TextField(I18n.t("username"));
		passwordField = new PasswordField(I18n.t("password"));
		rememberMe = new CheckBox(I18n.t("rememberMe"));
		login = new Button(I18n.t("login"));
		loginForm.addComponent(userName);
		loginForm.addComponent(passwordField);
		loginForm.addComponent(new LanguageComboBox(getUI().getLocale()));
		loginForm.addComponent(rememberMe);
		loginForm.addComponent(login);
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setDisableOnClick(true);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addClickListener(event -> login());

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSpacing(true);
		loginLayout.setSizeUndefined();

		if (request.getParameter("logout") != null) {
			loggedOutLabel = new Label(I18n.t("loggedOut"));
			loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
			loggedOutLabel.setSizeUndefined();
			loginLayout.addComponent(loggedOutLabel);
			loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
		}

		loginLayout.addComponent(loginFailedLabel = new Label());
		loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
		loginFailedLabel.setSizeUndefined();
		loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
		loginFailedLabel.setVisible(false);

		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		VerticalLayout rootLayout = new VerticalLayout(loginLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		setContent(rootLayout);
		setSizeFull();
	}

	private void login() {
		try {
			vaadinSecurity.login(userName.getValue(), passwordField.getValue(),
					rememberMe.getValue());
		} catch (AuthenticationException ex) {
			userName.focus();
			userName.selectAll();
			passwordField.setValue("");
			loginFailedLabel.setValue(I18n.t("loginFailed"));
			loginFailedLabel.setVisible(true);
			if (loggedOutLabel != null) {
				loggedOutLabel.setVisible(false);
			}
		} catch (Exception ex) {
			BBPlay.error(ex.getMessage());
			logger.error("Unexpected error while logging in", ex);
		} finally {
			login.setEnabled(true);
		}
	}
}
