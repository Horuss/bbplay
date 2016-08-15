package pl.horuss.bbplay.web.parts;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class Footer extends VerticalLayout {

	private static final long serialVersionUID = -2105130582636980820L;

	public Footer() {
		addStyleName("footer");

		final Label content = new Label(
				"<strong>BBPlay</strong> \u00a9 2016 by <a href=\"http://www.horuss.pl\">Horuss</a> | Please report <a href=\"https://github.com/Horuss/bbplay/issues\">bugs & features</a>");
		content.setContentMode(ContentMode.HTML);
		content.setWidthUndefined();
		addComponent(content);
	}

}
