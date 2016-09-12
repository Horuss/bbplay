package pl.horuss.bbplay.web.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtil {

	public static boolean isAdmin() {
		return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
				.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public static boolean isCurrentUser(String username) {
		return SecurityContextHolder.getContext().getAuthentication().getName().equals(username);
	}

	/**
	 * Util method for manually generating passwords
	 */
	public static void main(String[] args) {
		String password = "someNewPassword";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode(password));
	}

}
