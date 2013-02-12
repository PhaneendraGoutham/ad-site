package pl.stalkon.ad.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.ext.SocialUserDataFetcherFactory;
import pl.stalkon.social.model.AbstractSocialUserServiceImpl;

public class SocialUserServiceImpl extends AbstractSocialUserServiceImpl {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SocialUserDataFetcherFactory socialUserDataFetcherFactory;
	
	@Override
	public String execute(Connection<?> connection) {
		// First check to see if there already is a user for this email address.
		// If there is, return that user id
		// If there isn't, create a new user, activate him and send a welcome
		// email.
		
		UserProfile profile = connection.fetchUserProfile();
		// If we can't get the users email, e.g. twitter
		if (profile.getEmail() == null || profile.getFirstName() == null
				|| profile.getLastName() == null)
			return null;

		User user = null;
		user = userService.getUserByMailOrUsername(profile.getEmail());
		if (user != null)
			return user.getCredentials().getMail();
		SocialUserDataFetcher fetcher = socialUserDataFetcherFactory
				.getFetcher(connection.createData().getProviderId());
		if (fetcher == null) {
			return null;
		}
		user = (User) fetcher.fetchData(connection.getApi());
		if (user == null) {
			return null;
		}

		user = userService.add(user);
		return user.getCredentials().getUsername();
	}

}
