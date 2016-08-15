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
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Change password", order = 0)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class ChangePasswordOperation implements Runnable {

	private final SimpleService backend;

	@Autowired
	public ChangePasswordOperation(SimpleService backend) {
		this.backend = backend;
	}

	@Override
	public void run() {
		Notification.show(backend.echo("Not working yet"));
	}

}
