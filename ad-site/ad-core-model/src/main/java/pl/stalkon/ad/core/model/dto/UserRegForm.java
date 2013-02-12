package pl.stalkon.ad.core.model.dto;

import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;

public class UserRegForm extends pl.styall.library.core.form.UserRegForm<UserData, Address> {

	private static final long serialVersionUID = -2086236119630399425L;
	
	private DisplayNameType displayNameType;

	public DisplayNameType getDisplayNameType() {
		return displayNameType;
	}

	public void setDisplayNameType(DisplayNameType displayNameType) {
		this.displayNameType = displayNameType;
	}
	

}
