package pl.stalkon.ad.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.SocialUser;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.security.SocialLoggedUser.LoggedType;
import pl.stalkon.social.singleconnection.interfaces.SocialUserDetails;
import pl.styall.library.core.model.UserRole;


@Service("userDetailsService")
public class SocialUserDetailsServiceImpl implements MixUserDetailsService {

	@Autowired
	private SocialUserDao socialUserDao;

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public SocialUserDetails loadUserByUserId(Long userId) {
		User user = userDao.get(userId);
		return createSocialUserDetails(user);
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = userDao.loadUserByLogin(username);
		return createSocialUserDetails(user);
	}
	
	private SocialUserDetails createSocialUserDetails(User user){
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		List<String> roles = new ArrayList<String>();
		List<UserRole> userRoles = user.getUserRoles();
		for (UserRole ur : userRoles) {
			roles.add(ur.getRole());
		}
		LoggedType type;
		if (user.getSocialUser() != null) {
			type = LoggedType.SOCIAL;
		} else {
			type = LoggedType.API;
		}
		return new SocialLoggedUser(user.getId(), user.getCredentials()
				.getUsername(), user.getDisplayName(), user.getUserData().getBirthDate(), user
				.getCredentials().getPassword(), user.getCredentials()
				.getSalt(), user.getUserData().getImageUrl(), type, getAuthorities(roles));
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
