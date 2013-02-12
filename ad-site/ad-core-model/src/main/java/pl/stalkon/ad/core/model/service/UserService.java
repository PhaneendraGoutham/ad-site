package pl.stalkon.ad.core.model.service;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.styall.library.core.model.service.AbstractUserService;

public interface UserService extends AbstractUserService<User> {
	public User register(UserRegForm userRegoForm);
}
