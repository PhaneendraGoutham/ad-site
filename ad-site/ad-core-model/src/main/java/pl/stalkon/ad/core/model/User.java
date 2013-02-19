package pl.stalkon.ad.core.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;


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


	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="user")
	private SocialUser socialUser;


	public SocialUser getSocialUser() {
		return socialUser;
	}

	public void setSocialUser(SocialUser socialUser) {
		this.socialUser = socialUser;
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
			if (getSocialUser() != null ) {
				String name = getSocialUser().getDisplayName();
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
