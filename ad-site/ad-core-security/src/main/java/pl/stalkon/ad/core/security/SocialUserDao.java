package pl.stalkon.ad.core.security;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.SocialUser;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.social.singleconnection.interfaces.RemoteUser;

@Repository
public class SocialUserDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public RemoteUser get(String providerId, String providerUserId) {
		return (SocialUser) currentSession()
				.createQuery(
						"from SocialUser where providerId = :providerId and providerUserId = :providerUserId")
				.setString("providerId", providerId)
				.setString("providerUserId", providerUserId).uniqueResult();
	}

	public RemoteUser get(Long userId) {
		return (SocialUser) currentSession().createQuery(
				"from SocialUser where userId = :userId").setLong(
				"userId", userId);
	}

	public RemoteUser get(Long userId, String providerId, String providerUserId) {
		return (SocialUser) currentSession()
				.createQuery(
						"from SocialUser where providerId = :providerId and providerUserId = :providerUserId and userId = :userId")
				.setString("providerId", providerId)
				.setString("providerUserId", providerUserId)
				.setLong("userId", userId).uniqueResult();
	}

	public RemoteUser save(RemoteUser remoteUser) {
		SocialUser user = (SocialUser) remoteUser;
		currentSession().saveOrUpdate(user);
		return user;
	}

	@SuppressWarnings("unchecked")
	public boolean isUserIdConnectedTo(String providerId, Long userId) {
		List<SocialUser> list = currentSession()
		.createQuery(
				"from SocialUser where providerId = :providerId and userId = :userId")
		.setString("providerId", providerId)
		.setLong("userId", userId).list();
		if(list.size() > 0)
			return true;
		return false;
	}

	public RemoteUser createRemoteUser(User user, String providerId,
			String providerUserId, String displayName, String profileUrl,
			String accessToken, String secret, String refreshToken,
			Long expireTime) {
		SocialUser su = new SocialUser(user, providerId, providerUserId, displayName, profileUrl, accessToken, secret, refreshToken, expireTime);
		this.save(su);
		return su;
	}

	protected Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

}
