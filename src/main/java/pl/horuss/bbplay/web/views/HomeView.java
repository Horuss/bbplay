package pl.horuss.bbplay.web.views;

import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import pl.horuss.bbplay.web.Sections;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Home", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class HomeView extends VerticalLayout implements View {

	private static final long serialVersionUID = 454182473938094294L;

	public HomeView() {

		setSpacing(true);
		setMargin(true);

		Label header = new Label("BBPlay");
		header.addStyleName(ValoTheme.LABEL_H1);
		addComponent(header);

		Label body = new Label(
				"Welcome to BBPlay - Basketball Team Manager!<br/>Please report found bugs and proposed features <a href=\"https://github.com/Horuss/bbplay/issues\" target=\"_blank\">here</a>",
				ContentMode.HTML);
		addComponent(body);

	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
