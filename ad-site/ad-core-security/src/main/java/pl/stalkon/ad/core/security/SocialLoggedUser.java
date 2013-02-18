package pl.stalkon.ad.core.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUserDetails;

import pl.styall.library.core.security.authentication.LoggedUser;

public class SocialLoggedUser extends LoggedUser implements SocialUserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255498365790416613L;
	
	public enum LoggedType {API,SOCIAL};
	
	private String displayName;
	
	private final LoggedType type;

	public SocialLoggedUser(Long id, String username,
			String password, String salt, String imageUrl, LoggedType type,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, password, salt, imageUrl, authorities);
		this.type = type;
	}
	
	public SocialLoggedUser(Long id, String username,
			String password, String salt, String imageUrl,LoggedType type, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, password, salt, imageUrl, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		this.type = type;
	}


	@Override
	public String getUserId() {
		return getUsername();
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public LoggedType getType() {
		return type;
	}
	
}
