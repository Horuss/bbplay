package pl.horuss.bbplay.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.spring.i18n.MessageProvider;
import org.vaadin.spring.i18n.ResourceBundleMessageProvider;
import org.vaadin.spring.i18n.annotation.EnableI18N;

@Configuration
@EnableI18N
public class ApplicationConfiguration {

	@Bean
	MessageProvider communicationMessages() {
		return new ResourceBundleMessageProvider("i18n/messages");
	}

}
