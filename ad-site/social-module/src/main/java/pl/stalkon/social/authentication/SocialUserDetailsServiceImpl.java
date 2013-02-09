package pl.stalkon.social.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.social.model.SocialUserDao;
import pl.styall.library.core.model.AbstractUser;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.security.authentication.LoggedUser.LoggedType;
import pl.styall.library.core.security.authentication.UserDetailsServiceImpl;

public class SocialUserDetailsServiceImpl extends UserDetailsServiceImpl implements SocialUserDetailsService{

	@Autowired
	private SocialUserDao socialUserDao;
	
	@Override
	@Transactional
	public SocialUserDetails loadUserByUserId(String userId)
			throws UsernameNotFoundException, DataAccessException {
		AbstractUser user = socialUserDao.getUser(userId);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		List<String> roles = new ArrayList<String>();
		List<UserRole> userRoles = user.getUserRoles();
		for (UserRole ur : userRoles) {
			roles.add(ur.getRole());
		}
		return new SocialLoggedUser(user.getId(), user.getCredentials().getUsername(), user
				.getCredentials().getMail(), user.getCredentials()
				.getPassword(), user.getCredentials().getSalt(), LoggedType.SOCIAL,
				getAuthorities(roles));
	}

}
