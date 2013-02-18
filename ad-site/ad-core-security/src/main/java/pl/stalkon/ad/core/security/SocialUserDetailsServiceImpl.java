package pl.stalkon.ad.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.security.SocialLoggedUser.LoggedType;
import pl.stalkon.social.model.SocialUser;
import pl.stalkon.social.model.SocialUserDao;
import pl.styall.library.core.model.AbstractUser;
import pl.styall.library.core.model.UserRole;

@Service("userDetailsService")
public class SocialUserDetailsServiceImpl implements MixUserDetailsService {

	@Autowired
	private SocialUserDao socialUserDao;

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public SocialUserDetails loadUserByUserId(String userId)
			throws UsernameNotFoundException, DataAccessException {
		User user = userDao.loadUserByLogin(userId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		List<String> roles = new ArrayList<String>();
		List<UserRole> userRoles = user.getUserRoles();
		for (UserRole ur : userRoles) {
			roles.add(ur.getRole());
		}
		List<SocialUser> socialUsers = user.getSocialUsers();
		String imageUrl;
		LoggedType type;
		if (socialUsers != null && socialUsers.size() > 0) {
			imageUrl = socialUsers.get(0).getImageUrl();
			type = LoggedType.SOCIAL;
		} else {
			imageUrl = user.getUserData().getImageUrl();
			type = LoggedType.API;
		}
		System.out.println(type);
		return new SocialLoggedUser(user.getId(), user.getCredentials()
				.getUsername(), user
				.getCredentials().getPassword(), user.getCredentials()
				.getSalt(), imageUrl, type, getAuthorities(roles));
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println(username);
		return loadUserByUserId(username);
	}

	protected Collection<SimpleGrantedAuthority> getAuthorities(
			List<String> userRoles) {
		List<SimpleGrantedAuthority> authList = new ArrayList<SimpleGrantedAuthority>();
		for (String ur : userRoles) {
			authList.add(new SimpleGrantedAuthority(ur));
		}
		return authList; // TODO Auto-generated method stub
	}
}
