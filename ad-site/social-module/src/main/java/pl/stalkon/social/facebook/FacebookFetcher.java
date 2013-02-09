package pl.stalkon.social.facebook;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;

import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.styall.library.core.model.AbstractUserData.Sex;
import pl.styall.library.core.model.Credentials;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.core.model.defaultimpl.UserRegForm;
import pl.styall.library.core.model.defaultimpl.UserService;

public class FacebookFetcher implements SocialUserDataFetcher<Facebook> {

	private static Logger log = Logger.getLogger(FacebookFetcher.class);

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Override
	public User fetchData(Facebook api) {
		FacebookProfile profile = api.userOperations().getUserProfile();
		UserRegForm userRegForm = new UserRegForm();
		userRegForm.setMail(profile.getEmail());
		String password = RandomStringUtils.random(12,0,0,true,true,null, new SecureRandom());
		userRegForm.setPassword(password);
		userRegForm.setConfirmPassword(password);
		userRegForm.setUsername(profile.getUsername().replace(".", ""));
		UserData data = new UserData();
		String birthday = profile.getBirthday();
		SimpleDateFormat format = new SimpleDateFormat("MM/DD/YYYY");
		try {
			data.setBirthDate(format.parse(birthday));
		} catch (ParseException e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		data.setName(profile.getFirstName());
		data.setSurname(profile.getLastName());
		if (profile.getGender() != null) {
			if (profile.getGender().equals("male")) {
				data.setSex(Sex.MALE);
			} else {
				data.setSex(Sex.FEMALE);
			}
		}
		userRegForm.setUserData(data);
		User user = userService.register(userRegForm);
		return user;
	}

}
