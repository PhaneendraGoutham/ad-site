package pl.stalkon.social.authentication;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import pl.styall.library.core.security.authentication.LoggedUser;

public class SocialLoggedUser extends LoggedUser implements SocialUserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2503266021735149826L;

	public SocialLoggedUser(Long id, String username, String mail,
			String password, String salt, LoggedType type, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, mail, password, salt, type, enabled,
				accountNonExpired, credentialsNonExpired, accountNonLocked,
				authorities);
	}

	public SocialLoggedUser(Long id, String username, String mail,
			String password, String salt, LoggedType type,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, mail, password, salt, type, authorities);
	}

	@Override
	public String getUserId() {
		return getUsername();
	}
}
