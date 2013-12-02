package pl.stalkon.ad.core.model.service.impl;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.dto.UserAddressDto;
import pl.stalkon.ad.core.model.dto.UserProfileDto;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.model.Credentials;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.core.model.service.impl.AbstractUserServiceImpl;
import pl.styall.library.core.security.rest.UserLoginsService;

@Service("userService")
public class UserServiceImpl extends AbstractUserServiceImpl<User> implements
		UserService, UserLoginsService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	@Transactional
	public User register(UserRegForm userRegForm) {
		User user = new User();
		Credentials credentials = new Credentials();
		String password = userRegForm.getPassword();

		credentials.setToken();
		credentials.setPassword(passwordEncoder.encode(password));
		credentials.setMail(userRegForm.getMail());
		credentials.setUsername(userRegForm.getUsername());
		credentials.setActive(false);// TODO:
		UserData userData = new UserData();
		userData.setBirthDate(userRegForm.getBirthdate());
		userData.setSex(userRegForm.getSex());
		userData.setName(userRegForm.getName());
		userData.setSurname(userRegForm.getSurname());
		userData.setImageUrl("/resources/img/no-user.gif");
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

	@Override
	@Transactional
	public User getWithAddresses(Long userId) {
		User user = userDao.get(userId);
		user.getAddresses().size();
		return user;
	}

	@Override
	@Transactional
	public void updateUserAddress(UserAddressDto userAddressDto, Long userId) {
		User user = userDao.get(userId);
		user.getUserData().setSurname(userAddressDto.getSurname());
		user.getUserData().setName(userAddressDto.getFirstname());
		if (user.getAddresses().size() > 0) {
			Address address = user.getAddresses().get(0);
			address.setCity(userAddressDto.getAddress().getCity());
			address.setZip(userAddressDto.getAddress().getZip());
			address.setStreet(userAddressDto.getAddress().getStreet());
			address.setHomeNr(userAddressDto.getAddress().getHomeNr());
		} else {
			user.addAddress(userAddressDto.getAddress());
		}
		userDao.update(user);

	}

	@Override
	@Transactional
	public String generateAndSetNewPassword(String mail) {
		User user = getUserByMailOrUsername(mail);
		if (user != null) {
			String newPassword = KeyGenerators.string().generateKey();
			user.getCredentials().setPassword(
					passwordEncoder.encode(newPassword));
			userDao.save(user);
			return newPassword;
		}
		return null;
	}

	@Override
	@Transactional
	public List<String> getUserLogins(String login) {
		User user = getUserByMailOrUsername(login);
		return Arrays.asList(user.getCredentials().getMail(), user.getCredentials().getUsername());
	}

}
