package pl.horuss.bbplay.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BBPlay {

	public static void main(String[] args) {
		SpringApplication.run(BBPlay.class, args);
	}

}
