package pl.horuss.bbplay.web.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

	public static boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("ADMIN"));
	}

	public static boolean isCurrentUser(String username) {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
	}

}
