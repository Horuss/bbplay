package pl.horuss.bbplay.web.views.error;

import com.vaadin.navigator.View;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class AbstractErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = -2668759355846175232L;

	private Label message;

	public AbstractErrorView() {
		setMargin(true);
		message = new Label();
		message.setSizeUndefined();
		message.addStyleName(ValoTheme.LABEL_FAILURE);
		addComponent(message);
	}

	protected void setError(String s) {
		message.setValue(s);
	}

}
