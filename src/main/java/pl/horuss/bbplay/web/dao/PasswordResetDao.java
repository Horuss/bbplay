package pl.horuss.bbplay.web.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import pl.horuss.bbplay.web.model.PasswordReset;
import pl.horuss.bbplay.web.model.User;

public interface PasswordResetDao extends CrudRepository<PasswordReset, Long> {

	public PasswordReset findByLink(String link);

	@Transactional
	public List<PasswordReset> removeByUser(User user);

}
