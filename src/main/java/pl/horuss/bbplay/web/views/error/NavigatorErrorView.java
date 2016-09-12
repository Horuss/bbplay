package pl.horuss.bbplay.web.views.error;

import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * No Spring-managed because instantiation by Vaadin Navigator
 */
public class NavigatorErrorView extends AbstractErrorView {

	private static final long serialVersionUID = 4959983504395007659L;

	@Override
	public void enter(ViewChangeEvent event) {
		setError(I18n.t("error.noView", event.getViewName()));
	}

}
