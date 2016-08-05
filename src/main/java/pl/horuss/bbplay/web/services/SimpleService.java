package pl.horuss.bbplay.web.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class SimpleService {

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String adminOnlyEcho(String s) {
		return "admin:" + s;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public String echo(String s) {
		return s;
	}

}
