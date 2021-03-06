package pl.horuss.bbplay.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.horuss.bbplay.web.model.User;
import pl.horuss.bbplay.web.utils.Cookies;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BBPlay {

	public static void main(String[] args) {

		Locale.setDefault(Locale.ENGLISH);

		checkRequiredParameters("db.url", "db.username", "db.password");

		SpringApplication.run(BBPlay.class, args);
	}

	public static User currentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static void info(String description) {
		Notification notification = new Notification(I18n.t("info"), description,
				Notification.Type.HUMANIZED_MESSAGE);
		notification.setPosition(Position.TOP_CENTER);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}

	public static void error(String description) {
		Notification notification = new Notification(I18n.t("error"), description,
				Notification.Type.ERROR_MESSAGE);
		notification.setPosition(Position.TOP_CENTER);
		notification.setDelayMsec(2000);
		notification.show(Page.getCurrent());
	}

	@Autowired
	private ServletContext servletContext;

	@PostConstruct
	public void init() throws IOException {
		String root = servletContext.getRealPath("/");
		if (root != null && Files.isDirectory(Paths.get(root))) {
			// workaround for https://dev.vaadin.com/ticket/18463
			Files.createDirectories(Paths.get(servletContext
					.getRealPath("/VAADIN/themes/bbplay-theme")));
		}
	}

	private static void checkRequiredParameters(String... params) {
		List<String> missingParams = new ArrayList<>();
		for (String s : params) {
			if (System.getProperty(s) == null) {
				missingParams.add(s);
			}
		}
		if (!missingParams.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following parameters are required:\n");
			for (String s : missingParams) {
				sb.append("\t" + s + "\n");
			}
			throw new IllegalArgumentException(sb.toString());
		}
	}

	public static Locale getLanguage(String languageHeader) {
		Cookie cookie = Cookies.getCookie("bbplay-lang");
		if (cookie == null || cookie.getValue() == null || cookie.getValue().isEmpty()) {
			cookie = Cookies.addCookie("bbplay-lang", new Locale(languageHeader).getLanguage());
		}
		return new Locale(cookie.getValue());
	}

	public static void setLanguage(String language) {
		Cookies.addCookie("bbplay-lang", language);
	}

}
