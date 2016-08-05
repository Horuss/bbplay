package pl.horuss.bbplay.web.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.services.SimpleService;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "User operation", order = 0)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class UserOperation implements Runnable {

	private final SimpleService backend;

	@Autowired
	public UserOperation(SimpleService backend) {
		this.backend = backend;
	}

	@Override
	public void run() {
		Notification.show(backend.echo("Hello World"));
	}

}
