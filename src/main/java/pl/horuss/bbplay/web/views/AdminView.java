package pl.horuss.bbplay.web.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.services.SimpleService;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;

@Secured("ROLE_ADMIN")
@SpringView(name = "admin")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Admin View")
@FontAwesomeIcon(FontAwesome.COGS)
public class AdminView extends CustomComponent implements View {

	private static final long serialVersionUID = -517267857575900484L;

	private final SimpleService simpleService;

	@Autowired
	public AdminView(SimpleService simpleService) {
		this.simpleService = simpleService;
		Button button = new Button("Call admin backend",
				event -> Notification.show(AdminView.this.simpleService
						.adminOnlyEcho("Hello Admin World!")));
		setCompositionRoot(button);
	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
