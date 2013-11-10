package pl.stalkon.ad.core.security;

import java.util.Collection;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Years;
import org.springframework.security.core.GrantedAuthority;

import pl.stalkon.social.singleconnection.interfaces.SocialUserDetails;
import pl.styall.library.core.security.authentication.LoggedUser;

public class SocialLoggedUser extends LoggedUser implements SocialUserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2255498365790416613L;
	
	public enum LoggedType {API,SOCIAL};
	
	private String displayName;
	private Boolean adult = false;

	private final LoggedType type;

	public SocialLoggedUser(Long id, String username, String displayName, Date birthdate,
			String password, String salt, String imageUrl, LoggedType type,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, password, salt, imageUrl, authorities);
		this.displayName = displayName;
		System.out.println(countYears(birthdate));
		if(countYears(birthdate) >= 18){
			this.adult = true;
		}
		this.type = type;
	}
	
	public SocialLoggedUser(Long id, String username,
			String password, String salt, String imageUrl,LoggedType type, Date birthdate, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(id, username, password, salt, imageUrl, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, authorities);
		System.out.println(countYears(birthdate));
		if(countYears(birthdate) >= 18){
			this.adult = true;
		}
		this.type = type;
	}


	@Override
	public Long getUserId() {
		return getId();
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

	public Boolean getAdult() {
		return adult;
	}

	public void setAdult(Boolean adult) {
		this.adult = adult;
	}
	
	
	private int countYears(Date birthdate){
		DateTime curr = new DateTime();
		DateTime birth = new DateTime(birthdate);
		Interval i = new Interval(birth, curr);
		Years years = Years.yearsIn(i);
		return years.getYears();
	}
}
