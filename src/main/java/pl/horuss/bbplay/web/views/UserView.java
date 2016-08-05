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

@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "user")
@SideBarItem(sectionId = Sections.VIEWS, caption = "User View")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class UserView extends CustomComponent implements View {

	private static final long serialVersionUID = 5957803852021722482L;

	private final SimpleService simpleService;

	@Autowired
	public UserView(SimpleService simpleService) {
		this.simpleService = simpleService;
		Button button = new Button("Call user backend",
				event -> Notification.show(UserView.this.simpleService.echo("Hello User World!")));
		setCompositionRoot(button);
	}

	@Override
	public void enter(ViewChangeEvent viewChangeEvent) {

	}

}
