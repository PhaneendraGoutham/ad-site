package pl.stalkon.ad.core.security;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.styall.library.core.model.AbstractUserData.Sex;

public class FacebookFetcher implements SocialUserDataFetcher<Facebook> {

	private static Logger log = Logger.getLogger(FacebookFetcher.class);

	
	@Autowired
	private UserService userService;
	
	@Override
	public User fetchData(Facebook api) {
		FacebookProfile profile = api.userOperations().getUserProfile();
		UserRegForm userRegForm = new UserRegForm();
		userRegForm.setMail(profile.getEmail());
		String password = KeyGenerators.string().generateKey();
		String username = RandomStringUtils.random(10,0,35,true,true,"abcdefghijklmnopqrstuvwxyz1234567890".toCharArray(), new SecureRandom());
		userRegForm.setPassword(password);
		userRegForm.setConfirmPassword(password);
		userRegForm.setUsername(username);
		String birthday = profile.getBirthday();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		try {
			userRegForm.setBirthdate(format.parse(birthday));
		} catch (ParseException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		userRegForm.setName(profile.getFirstName());
		userRegForm.setSurname(profile.getLastName());
		if (profile.getGender() != null) {
			if (profile.getGender().equals("male")) {
				userRegForm.setSex(Sex.MALE);
			} else {
				userRegForm.setSex(Sex.FEMALE);
			}
		}
		userRegForm.setDisplayNameType(DisplayNameType.SOCIAL_DISPLAY_NAME);
		User user = userService.register(userRegForm);
		return user;
	}

}
