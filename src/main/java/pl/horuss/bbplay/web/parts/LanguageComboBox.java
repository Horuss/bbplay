package pl.horuss.bbplay.web.parts;

import java.util.Locale;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.utils.I18n;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.ComboBox;

public class LanguageComboBox extends ComboBox {

	private static final long serialVersionUID = 5647280034543192959L;

	public LanguageComboBox(Locale locale) {
		super(I18n.t("language"));
		setStyleName("languages");
		setContainerDataSource(createDataSource());
		setItemCaptionPropertyId("caption");
		setItemIconPropertyId("icon");
		setImmediate(true);
		setNullSelectionAllowed(false);

		setInitialValue(locale);

		addValueChangeListener(event -> {
			BBPlay.setLanguage(((Locale) event.getProperty().getValue()).getLanguage());
			Page.getCurrent().reload();
		});

	}

	private void setInitialValue(Locale language) {
		for (Object ol : getContainerDataSource().getItemIds()) {
			Locale l = (Locale) ol;
			if (l.getLanguage().equals(language.getLanguage())) {
				setValue(ol);
				return;
			}
		}
		setValue(getContainerPropertyIds().iterator().next());
	}

	private Container createDataSource() {
		Container languageItems = new IndexedContainer();
		languageItems.addContainerProperty("icon", ThemeResource.class, null);
		languageItems.addContainerProperty("caption", String.class, "");

		fillItem(languageItems.addItem(Locale.ENGLISH), "English", "en");
		fillItem(languageItems.addItem(new Locale("pl")), "Polski", "pl");

		return languageItems;
	}

	@SuppressWarnings("unchecked")
	private void fillItem(Item item, String langCode, String icon) {
		item.getItemProperty("icon").setValue(new ThemeResource("icons/" + icon + ".png"));
		item.getItemProperty("caption").setValue(langCode);
	}
}
