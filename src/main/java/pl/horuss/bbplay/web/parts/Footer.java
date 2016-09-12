package pl.horuss.bbplay.web.parts;

import java.time.Year;

import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Footer extends VerticalLayout {

	private static final long serialVersionUID = -2105130582636980820L;

	public Footer() {
		addStyleName("footer");

		String version = Footer.class.getPackage().getImplementationVersion();
		final Label content = new Label(
				"<strong>BBPlay</strong> (v. <a href=\"https://github.com/Horuss/bbplay/releases/tag/"
						+ version + "\" target=\"_blank\">" + version + "</a>) \u00a9 2016"
						+ (Year.now().getValue() != 2016 ? " - " + Year.now().getValue() : "")
						+ " by <a href=\"http://www.horuss.pl\" target=\"_blank\">Horuss</a> | "
						+ I18n.t("footer.text")
						+ " <a href=\"https://github.com/Horuss/bbplay/issues\" target=\"_blank\">"
						+ I18n.t("footer.linkText") + "</a>");
		content.setContentMode(ContentMode.HTML);
		content.setWidthUndefined();
		addComponent(content);
	}

}
