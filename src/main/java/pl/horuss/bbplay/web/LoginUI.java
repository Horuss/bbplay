package pl.horuss.bbplay.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

import pl.horuss.bbplay.web.parts.ForgetPasswordWindow;
import pl.horuss.bbplay.web.parts.LanguageComboBox;
import pl.horuss.bbplay.web.services.UserService;
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

	@Autowired
	private UserService userService;

	private TextField userName;

	private PasswordField passwordField;

	private CheckBox rememberMe;

	private Button login;
	
	private Button forgetPassword;

	private Label msgLabel;

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
		forgetPassword = new Button(I18n.t("forgetPassword"));
		
		loginForm.addComponent(userName);
		loginForm.addComponent(passwordField);
		loginForm.addComponent(new LanguageComboBox(getUI().getLocale()));
		loginForm.addComponent(rememberMe);
		loginForm.addComponent(login);
		loginForm.addComponent(forgetPassword);
		
		forgetPassword.addStyleName(ValoTheme.BUTTON_LINK);
		forgetPassword.addClickListener(event -> UI.getCurrent().addWindow(new ForgetPasswordWindow(userService)));
		
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setDisableOnClick(true);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addClickListener(event -> login());

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSpacing(true);
		loginLayout.setSizeUndefined();
		
		loginLayout.addComponent(msgLabel = new Label());
		loginLayout.setComponentAlignment(msgLabel, Alignment.BOTTOM_CENTER);
		msgLabel.setSizeUndefined();
		msgLabel.setVisible(false);

		if (request.getParameter("logout") != null) {
			msgLabel.setValue(I18n.t("loggedOut"));
			msgLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
			msgLabel.setVisible(true);
		}
		
		if (request.getParameter("changed") != null) {
			msgLabel.setValue(I18n.t("changePassword.success"));
			msgLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
			msgLabel.setVisible(true);
		}

		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		VerticalLayout rootLayout = new VerticalLayout(loginLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
		setContent(rootLayout);
		setSizeFull();
	}

	private void login() {
		login.setEnabled(false);
		try {
			vaadinSecurity.login(userName.getValue(), passwordField.getValue(),
					rememberMe.getValue());
		} catch (AuthenticationException ex) {
			userName.focus();
			userName.selectAll();
			passwordField.setValue("");
			msgLabel.setValue(I18n.t("loginFailed"));
			msgLabel.addStyleName(ValoTheme.LABEL_FAILURE);
			msgLabel.setVisible(true);
		} catch (Exception ex) {
			BBPlay.error(ex.getMessage());
			logger.error("Unexpected error while logging in", ex);
		} finally {
			login.setEnabled(true);
		}
	}
}
