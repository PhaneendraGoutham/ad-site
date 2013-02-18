package pl.stalkon.ad.core.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Loader;

import pl.stalkon.social.model.SocialUser;
import pl.styall.library.core.model.AbstractUser;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;

@Entity
@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "mail" }) })
public class User extends AbstractUser<UserData, Address> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3860832276542051232L;

	public enum DisplayNameType {
		NAME, REVERSED_NAME, EMAIL, SOCIAL_DISPLAY_NAME, USERNAME
	};

	@NotNull
	private DisplayNameType displayNameType;

	@Transient
	private String displayName;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="userId", referencedColumnName="username")
	private List<SocialUser> socialUsers;

	public List<SocialUser> getSocialUsers() {
		return socialUsers;
	}

	public void setSocialUsers(List<SocialUser> socialUsers) {
		this.socialUsers = socialUsers;
	}

	public String getDisplayName() {
		switch (displayNameType) {
		case NAME:
			if (getUserData().getName() != null
					&& !getUserData().getName().isEmpty()
					&& getUserData().getSurname() != null
					&& !getUserData().getSurname().isEmpty())
				return getUserData().getName() + " "
						+ getUserData().getSurname();
			else
				break;
		case REVERSED_NAME:
			if (getUserData().getName() != null
					&& !getUserData().getName().isEmpty()
					&& getUserData().getSurname() != null
					&& !getUserData().getSurname().isEmpty())
				return getUserData().getSurname() + " "
						+ getUserData().getName();
			else
				break;
		case EMAIL:
			return getCredentials().getMail();
		case SOCIAL_DISPLAY_NAME:
			if (getSocialUsers() != null && getSocialUsers().size() > 0) {
				String name = getSocialUsers().get(0).getDisplayName();
				if (name != null && !name.isEmpty())
					return name;
				else
					break;
			} else
				break;
		default:
			return getCredentials().getUsername();
		}
		return getCredentials().getUsername();
	}

	public DisplayNameType getDisplayNameType() {
		return displayNameType;
	}

	public void setDisplayNameType(DisplayNameType displayNameType) {
		this.displayNameType = displayNameType;
	}

}
