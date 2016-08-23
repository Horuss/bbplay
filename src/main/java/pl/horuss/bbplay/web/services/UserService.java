package pl.horuss.bbplay.web.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.dao.UserDao;
import pl.horuss.bbplay.web.model.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByName(username);
		if (null == user) {
			throw new UsernameNotFoundException("No such user: " + username);
		} else {
			return user;
		}
	}

	public String changePassword(String currentPassword, String newPassword) {
		User user = BBPlay.currentUser();
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return "Current password invalid";
		}

		if (!Pattern.compile("((?=.*\\d).{8,})").matcher(newPassword).matches()) {
			return "Password must be at least 8 characters long and contain digit";
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userDao.save(user);
		return null;

	}

}