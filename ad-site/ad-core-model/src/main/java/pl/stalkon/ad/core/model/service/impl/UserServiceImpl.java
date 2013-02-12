package pl.stalkon.ad.core.model.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.model.Credentials;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.core.model.service.impl.AbstractUserServiceImpl;

@Service("userService")
public class UserServiceImpl extends AbstractUserServiceImpl<User> implements UserService {

	@Override
	@Transactional
	public User register(UserRegForm userRegForm) {
		User user = new User();
		Credentials credentials = new Credentials();
		String password = userRegForm.getPassword();
		Address address = userRegForm.getAddress();

		credentials.setSalt();
		credentials.setToken();
		credentials.setPassword(passwordEncoder.encodePassword(password,
				credentials.getSalt()));
		credentials.setMail(userRegForm.getMail());
		credentials.setUsername(userRegForm.getUsername());
		UserData userData = userRegForm.getUserData();
		user.setUserData(userData);
		userData.addAddress(address);
		UserRole userRole = userDao.loadUserRoleByName("ROLE_USER");
		if (userRole == null) {
			userRole = new UserRole();
			userRole.setRole("ROLE_USER");
		}
		user.addUserRole(userRole);
		user.setCredentials(credentials);
		
		if(userRegForm.getDisplayNameType() != null){
			user.setDisplayNameType(userRegForm.getDisplayNameType());
		}else{
			user.setDisplayNameType(DisplayNameType.USERNAME);
		}
		userDao.save(user);
		return user;
	}
	
}
