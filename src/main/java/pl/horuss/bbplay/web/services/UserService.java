package pl.horuss.bbplay.web.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.horuss.bbplay.web.dao.UserDao;
import pl.horuss.bbplay.web.model.User;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByName(username);
		if (null == user) {
			throw new UsernameNotFoundException("No such user: " + username);
		} else {
			return user;
		}
	}

}