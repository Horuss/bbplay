package pl.horuss.bbplay.web.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class ErrorView extends VerticalLayout implements View {

	private static final long serialVersionUID = 4959983504395007659L;

	private Label message;

	public ErrorView() {
		setMargin(true);
		message = new Label();
		addComponent(message);
		message.setSizeUndefined();
		message.addStyleName(ValoTheme.LABEL_FAILURE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		message.setValue(String.format("No such view: %s", event.getViewName()));
	}

}
