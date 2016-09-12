package pl.horuss.bbplay.web.parts;

import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class ConfirmWindow extends Window implements Button.ClickListener {

	private static final long serialVersionUID = 2159295726379932002L;

	private Listener listener;
	Button yes = new Button(I18n.t("yes"), this);
	Button no = new Button(I18n.t("no"), this);

	private ConfirmWindow(String title, Resource icon, String text, Listener listener) {
		super(" " + title);
		this.listener = listener;
		setIcon(icon);
		setWidth("350px");
		setModal(true);
		setClosable(true);
		setResizable(false);
		addCloseShortcut(KeyCode.ESCAPE);
		setContent(windowContent(text));
	}

	private VerticalLayout windowContent(String text) {
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);

		VerticalLayout content = new VerticalLayout();

		if (text != null) {
			content.addComponent(new Label(text));
			root.addComponent(content);
		}

		HorizontalLayout footer = new HorizontalLayout();
		footer.setWidth("100%");
		footer.setSpacing(true);
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);

		Label footerText = new Label();

		yes.addStyleName(ValoTheme.BUTTON_PRIMARY);

		footer.addComponents(footerText, yes, no);
		footer.setExpandRatio(footerText, 1);

		root.addComponent(footer);

		return root;
	}

	public static void show(String title, Resource icon, String text, Listener listener) {
		UI.getCurrent().addWindow(
				new ConfirmWindow(title == null ? "" : title, icon == null ? FontAwesome.QUESTION
						: icon, text, listener));
	}

	public void buttonClick(ClickEvent event) {
		ConfirmWindow.this.close();
		listener.onClose(event.getSource().equals(yes));
	}

	public interface Listener {
		void onClose(boolean result);
	}

}
