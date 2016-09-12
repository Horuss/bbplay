package pl.horuss.bbplay.web.views.error;

import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class AccessDeniedView extends AbstractErrorView {

	private static final long serialVersionUID = 6308399641983248423L;

	@Override
	public void enter(ViewChangeEvent event) {
		setError(I18n.t("error.noAccess", event.getViewName()));
	}
}
