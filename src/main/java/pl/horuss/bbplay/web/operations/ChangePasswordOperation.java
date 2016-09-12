package pl.horuss.bbplay.web.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;
import pl.horuss.bbplay.web.parts.ChangePasswordWindow;
import pl.horuss.bbplay.web.services.UserService;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.UI;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, captionCode = "changePassword", order = 0)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class ChangePasswordOperation implements Runnable {

	@Autowired
	private UserService userService;

	@Override
	public void run() {
		UI.getCurrent().addWindow(new ChangePasswordWindow(userService));
	}

}
