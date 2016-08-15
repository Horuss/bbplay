package pl.horuss.bbplay.web;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@Component
@SideBarSections({ @SideBarSection(id = Sections.VIEWS, caption = "Menu"),
		@SideBarSection(id = Sections.OPERATIONS, caption = "Actions") })
public class Sections {

	public static final String VIEWS = "views";
	public static final String OPERATIONS = "operations";

}
