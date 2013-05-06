package pl.stalkon.ad.core.model.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.dto.UserProfileDto;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.model.Credentials;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.core.model.service.impl.AbstractUserServiceImpl;

@Service("userService")
public class UserServiceImpl extends AbstractUserServiceImpl<User> implements
		UserService {

	@Override
	@Transactional
	public User register(UserRegForm userRegForm) {
		User user = new User();
		Credentials credentials = new Credentials();
		String password = userRegForm.getPassword();

		credentials.setSalt();
		credentials.setToken();
		credentials.setPassword(passwordEncoder.encodePassword(password,
				credentials.getSalt()));
		credentials.setMail(userRegForm.getMail());
		credentials.setUsername(userRegForm.getUsername());
		credentials.setActive(true);// TODO:
		UserData userData = new UserData();
		userData.setBirthDate(userRegForm.getBirthdate());
		userData.setSex(userRegForm.getSex());
		userData.setName(userRegForm.getName());
		userData.setSurname(userRegForm.getSurname());
		user.setUserData(userData);
		UserRole userRole = userDao.loadUserRoleByName(UserRoleDef.ROLE_USER);
		user.addUserRole(userRole);
		user.setCredentials(credentials);
		if (userRegForm.getDisplayNameType() != null)
			user.setDisplayNameType(userRegForm.getDisplayNameType());
		else
			user.setDisplayNameType(DisplayNameType.USERNAME);
		userDao.save(user);
		return user;
	}

	@Override
	@Transactional
	public void updateProfile(UserProfileDto userProfileDto, Long id) {
		User user = userDao.get(id);
		user.getUserData().setName(userProfileDto.getName());
		user.getUserData().setSurname(userProfileDto.getSurname());
		userDao.update(user);
	}

	@Override
	@Transactional
	public void setUserThumbnail(String url, Long id) {
		User user = userDao.get(id);
		user.getUserData().setImageUrl(url);
		userDao.update(user);
	}

}
