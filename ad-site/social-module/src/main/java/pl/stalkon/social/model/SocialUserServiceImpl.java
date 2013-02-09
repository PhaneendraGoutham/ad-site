package pl.stalkon.social.model;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.jpa.RemoteUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.ext.SocialUserDataFetcherFactory;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserService;

@Service
public class SocialUserServiceImpl implements SocialUserService{

	@Autowired
	private SocialUserDao socialUserDao;

	@Autowired
	private UserService userService;

	@Autowired
	private SocialUserDataFetcherFactory socialUserDataFetcherFactory;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String execute(Connection<?> connection) {
		// First check to see if there already is a user for this email address.
		// If there is, return that user id
		// If there isn't, create a new user, activate him and send a welcome
		// email.
		System.out.println("execute");
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
		user = fetcher.fetchData(connection.getApi());
		if (user == null) {
			return null;
		}

		user = userService.add(user);
		return user.getCredentials().getMail();

	}

	@Transactional
	public Set<String> findUsersConnectedTo(String providerId,
			Set<String> providerUserIds) {
		return socialUserDao.findUsersConnectedTo(providerId, providerUserIds);
	}

	@Transactional
	public List<RemoteUser> getPrimary(String userId, String providerId) {
		return socialUserDao.getPrimary(userId, providerId);
	}

	@Transactional
	public int getRank(String userId, String providerId) {
		return socialUserDao.getRank(userId, providerId);
	}

	@Transactional
	public List<RemoteUser> getAll(String userId,
			MultiValueMap<String, String> providerUsers) {
		return socialUserDao.getAll(userId, providerUsers);
	}

	@Transactional
	public List<RemoteUser> getAll(String userId) {
		return socialUserDao.getAll(userId);
	}

	@Transactional
	public List<RemoteUser> getAll(String userId, String providerId) {
		return socialUserDao.getAll(userId, providerId);
	}

	@Transactional
	public RemoteUser get(String userId, String providerId,
			String providerUserId) {
		return socialUserDao.get(userId, providerId, providerUserId);
	}

	@Transactional
	public List<RemoteUser> get(String providerId, String providerUserId)
			throws IncorrectResultSizeDataAccessException {
		return socialUserDao.get(providerId, providerUserId);
	}

	@Transactional
	public void remove(String userId, String providerId) {
		socialUserDao.remove(userId, providerId);
	}

	@Transactional
	public void remove(String userId, String providerId, String providerUserId) {
		socialUserDao.remove(userId, providerId, providerUserId);

	}

	@Transactional
	public RemoteUser createRemoteUser(String userId, String providerId,
			String providerUserId, int rank, String displayName,
			String profileUrl, String imageUrl, String accessToken,
			String secret, String refreshToken, Long expireTime) {
		RemoteUser user = socialUserDao.createRemoteUser(userId, providerId, providerUserId, rank, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime);
		return user;
	}

	@Transactional
	public RemoteUser save(RemoteUser user) {
		return socialUserDao.save(user);
	}

}
