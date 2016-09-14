package pl.horuss.bbplay.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.vaadin.jouni.animator.AnimatorProxy;
import org.vaadin.spring.i18n.I18N;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

import pl.horuss.bbplay.web.parts.Footer;
import pl.horuss.bbplay.web.utils.I18n;
import pl.horuss.bbplay.web.views.error.AccessDeniedView;
import pl.horuss.bbplay.web.views.error.NavigatorErrorView;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("bbplay-theme")
@Push(transport = Transport.WEBSOCKET)
@Widgetset("AppWidgetset")
public class MainUI extends UI {

	private static final long serialVersionUID = -3538867580654293827L;

	private AnimatorProxy animator;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VaadinSecurity vaadinSecurity;

	@Autowired
	SpringViewProvider springViewProvider;

	@Autowired
	ValoSideBar sideBar;

	@Autowired
	I18N i18n;

	public AnimatorProxy animator() {
		return animator;
	}

	@Override
	protected void init(VaadinRequest request) {
		getUI().setLocale(BBPlay.getLanguage(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)));
		I18n.init(i18n);
		getPage().setTitle("BBPlay");
		setErrorHandler(new DefaultErrorHandler() {
			private static final long serialVersionUID = -18984634551415736L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
					BBPlay.error(I18n.t("error.noAccess", "MainUI"));
				} else {
					super.error(event);
				}
			}
		});
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		layout.addComponent(horizontalLayout);
		layout.setExpandRatio(horizontalLayout, 1f);

		sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
		horizontalLayout.addComponent(sideBar);

		CssLayout viewContainer = new CssLayout();
		viewContainer.setSizeFull();
		viewContainer.addStyleName("scrollable-y");
		horizontalLayout.addComponent(viewContainer);
		horizontalLayout.setExpandRatio(viewContainer, 1f);

		Navigator navigator = new Navigator(this, viewContainer);
		springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		navigator.addProvider(springViewProvider);
		navigator.setErrorView(NavigatorErrorView.class);

		Footer footer = new Footer();
		layout.addComponent(footer);
		layout.addComponent(animator = new AnimatorProxy());

		setContent(layout);
	}
}
