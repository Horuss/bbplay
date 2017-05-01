package pl.horuss.bbplay.web.services;

import java.net.URI;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

import com.vaadin.ui.UI;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PasswordResetDao passwordResetDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

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

	public User sendPasswordReset(String login) throws MessagingException {

		User user = userDao.findByName(login);
		if (user == null) {
			user = userDao.findByEmail(login);
		}
		if (user == null || user.getEmail() == null) {
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

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(user.getEmail());
		helper.setFrom("BBPlay <no_reply@bbplay>");
		helper.setSubject(I18n.t("mail.passwordReset.subject"));
		URI location = UI.getCurrent().getPage().getLocation();
		String baseUrl = location.toString().substring(0,
				location.toString().indexOf(location.getPath()));
		helper.setText(I18n.t("mail.passwordReset.body", baseUrl + "/password?token=" + link), true);

		mailSender.send(message);
		return user;
	}

	public String resetPassword(User user, String password) {

		user.setPassword(passwordEncoder.encode(password));
		userDao.save(user);

		passwordResetDao.removeByUser(user);

		return null;

	}

	public User checkToken(String token) {
		try {
			UUID.fromString(token);
		} catch (Exception ex) {
			return null;
		}
		PasswordReset passwordReset = passwordResetDao.findByLink(token);
		if (passwordReset == null) {
			return null;
		}
		return passwordReset.getUser();
	}

}