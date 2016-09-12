package pl.horuss.bbplay.web.utils;

import org.vaadin.spring.i18n.I18N;

public class I18n {

	private static I18N i18n;

	public static synchronized void init(I18N i18n) {
		I18n.i18n = i18n;
	}

	public static String t(String code) {
		return i18n.get(code);
	}

	public static String t(String code, Object... arguments) {
		return i18n.get(code, arguments);
	}

}
