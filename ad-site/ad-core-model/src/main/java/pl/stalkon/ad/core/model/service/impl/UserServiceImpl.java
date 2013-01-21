package pl.stalkon.ad.core.model.service.impl;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.model.Credentials;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.core.model.defaultimpl.UserRegForm;



@Service("userService")
public class UserServiceImpl extends pl.styall.library.core.model.defaultimpl.UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ShaPasswordEncoder passwordEncoder;
	@Override
	@Transactional
	public User register(UserRegForm userRegForm){
		User user = new User();
		Credentials credentials = new Credentials();
		String password = userRegForm.getPassword();
		Address address = userRegForm.getAddress();
		
		credentials.setSalt();
		credentials.setToken();
		credentials
				.setPassword(
						passwordEncoder.encodePassword(password, credentials.getSalt()));
		credentials.setMail(userRegForm.getMail());
		credentials.setUsername(userRegForm.getUsername());
		UserData userData = userRegForm.getUserData();
		user.setUserData(userData);
		userData.addAddress(address);
		UserRole userRole = userDao.loadUserRoleByName("ROLE_USER");
		if(userRole == null){
			userRole = new UserRole();
			userRole.setRole("ROLE_USER");
		}
		user.addUserRole(userRole);
		user.setCredentials(credentials);
		userDao.save(user);
		return user;
	}
	
	@Override
	@Transactional
	public boolean chechMailExists(String mail) {
		return userDao.chechMailExists(mail);
	}
	
	@Override
	@Transactional
	public boolean changePassword(Long id, String oldPassword, String newPassword){
		User user = userDao.get(id);
		String encodedPassword = passwordEncoder.encodePassword(oldPassword, user.getCredentials().getSalt());
		if(encodedPassword.equals(user.getCredentials().getPassword())){
			user.getCredentials().setPassword(encodedPassword);
			userDao.save(user);
		}else{
			return false;
		}
		return true;
	}



//	@Override
//	public void addAddress(UUID userId, Address address) {
//		User user = userDao.get(userId);
//		user.addAddress(address);
//		userDao.save(user);
//	}

}
