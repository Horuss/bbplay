package pl.horuss.bbplay.web.services;

import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.BBPlay;
import pl.horuss.bbplay.web.dao.PasswordResetDao;
import pl.horuss.bbplay.web.dao.UserDao;
import pl.horuss.bbplay.web.model.PasswordReset;
import pl.horuss.bbplay.web.model.User;
import pl.horuss.bbplay.web.utils.I18n;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordResetDao passwordResetDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByNameWithRoles(username);
		if (null == user) {
			throw new UsernameNotFoundException("No such user: " + username);
		} else {
			return user;
		}
	}

	public String changePassword(String currentPassword, String newPassword) {
		User user = BBPlay.currentUser();
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			return I18n.t("wrongPassword");
		}

		if (!Pattern.compile("((?=.*\\d).{8,})").matcher(newPassword).matches()) {
			return I18n.t("wrongPasswordPattern");
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userDao.save(user);
		return null;

	}

	public User sendPasswordReset(String login) {

		User user = userDao.findByName(login);
		if (user == null) {
			user = userDao.findByEmail(login);
		}
		if (user == null) {
			return null;
		}

		PasswordReset existingPasswordReset = null;
		String link;
		do {
			existingPasswordReset = passwordResetDao
					.findByLink(link = UUID.randomUUID().toString());
		} while (existingPasswordReset != null);

		passwordResetDao.removeByUser(user);

		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setUser(user);
		passwordReset.setLink(link);
		passwordResetDao.save(passwordReset);

		// TODO actually send mail with password reset link (+ create service...)

		return user;
	}

}