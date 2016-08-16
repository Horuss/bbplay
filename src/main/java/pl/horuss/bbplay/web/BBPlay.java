package pl.horuss.bbplay.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BBPlay {

	public static void main(String[] args) {
		SpringApplication.run(BBPlay.class, args);
	}

	@Autowired
	private ServletContext servletContext;

	@PostConstruct
	public void init() throws IOException {
		String root = servletContext.getRealPath("/");
		if (root != null && Files.isDirectory(Paths.get(root))) {
			// workaround for https://dev.vaadin.com/ticket/18463
			Files.createDirectories(Paths.get(servletContext
					.getRealPath("/VAADIN/themes/bbplay-theme")));
		}
	}

}
