package pl.horuss.bbplay.web;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BBPlay {

	public static void main(String[] args) {

		checkRequiredParameters("db.url", "db.username", "db.password");

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

	private static void checkRequiredParameters(String... params) {
		List<String> missingParams = new ArrayList<>();
		for (String s : params) {
			if (System.getProperty("db.password") == null) {
				missingParams.add(s);
			}
		}
		if (!missingParams.isEmpty()) {
			StringBuilder sb = new StringBuilder("The following parameters are required:\n");
			for (String s : missingParams) {
				sb.append("\t" + s + "\n");
			}
			throw new IllegalArgumentException(sb.toString());
		}
	}

}
