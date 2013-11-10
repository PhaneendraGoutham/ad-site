package pl.stalkon.ad.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.social.ext.SocialServiceHelper;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.singleconnection.interfaces.ConnectionSignup;

public class ConnectionSignupImpl implements ConnectionSignup {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SocialServiceHelper socialUserDataFetcherFactory;
	

	@Override
	@Transactional
	public Long execute(Connection<?> connection) {
		UserProfile profile = connection.fetchUserProfile();
		if (profile.getEmail() == null || profile.getFirstName() == null
				|| profile.getLastName() == null)
			return null;

		User user = userDao.loadUserByLogin(profile.getEmail());
		if (user != null)
			return user.getId();
		SocialUserDataFetcher fetcher = socialUserDataFetcherFactory
				.getFetcher(connection.createData().getProviderId());
		if (fetcher == null) {
			return null;
		}
		user = (User) fetcher.fetchData(connection.getApi());
		user.getUserData().setImageUrl(connection.getImageUrl());
		if (user == null) {
			return null;
		}
		userDao.add(user);
		return user.getId();
	}
	

}
