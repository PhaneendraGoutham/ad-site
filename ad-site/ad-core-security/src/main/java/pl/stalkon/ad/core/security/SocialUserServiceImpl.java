package pl.stalkon.ad.core.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.SocialUser;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.ext.SocialServiceHelper;
import pl.stalkon.social.singleconnection.interfaces.ConnectionSignup;
import pl.stalkon.social.singleconnection.interfaces.RemoteUser;
import pl.stalkon.social.singleconnection.interfaces.SocialUserService;

@Transactional
public class SocialUserServiceImpl implements SocialUserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private SocialUserDao socialUserDao;

	@Override
	public RemoteUser get(String providerId, String providerUserId) {
		return socialUserDao.get(providerId, providerUserId);
	}

	@Override
	public RemoteUser get(Long userId) {
		return socialUserDao.get(userId);
	}

	@Override
	public RemoteUser get(Long userId, String providerId, String providerUserId) {
		return socialUserDao.get(userId, providerId, providerUserId);
	}

	@Override
	public RemoteUser save(RemoteUser remoteUser) {
		return socialUserDao.save(remoteUser);
	}

	@Override
	public boolean isUserIdConnectedTo(String providerId, Long userId) {
		return socialUserDao.isUserIdConnectedTo(providerId, userId);
	}

	@Override
	public RemoteUser createRemoteUser(Long userId, String providerId,
			String providerUserId, String displayName, String profileUrl,
			String accessToken, String secret, String refreshToken,
			Long expireTime) {
		User user = userDao.get(userId);
		SocialUser su = new SocialUser(user, providerId, providerUserId,
				displayName, profileUrl, accessToken, secret, refreshToken,
				expireTime);
		user.setSocialUser(su);
		System.out.println(user.getId());
		userDao.save(user);
		return su;
	}

}
