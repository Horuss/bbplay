package pl.horuss.bbplay.web.utils;

import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinService;

public class Cookies {

	public static Cookie getCookie(String name) {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}
		return null;
	}

	public static Cookie addCookie(String name, String value) {
		Cookie newCookie = getCookie(name);
		if (newCookie != null) {
			newCookie.setValue(value);
		} else {
			newCookie = new Cookie(name, value);
		}
		newCookie.setPath("/");
		newCookie.setMaxAge(60 * 60 * 24 * 365 * 10);
		VaadinService.getCurrentResponse().addCookie(newCookie);
		return newCookie;
	}

}
