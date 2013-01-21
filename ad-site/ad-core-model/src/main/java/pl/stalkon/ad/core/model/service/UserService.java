package pl.stalkon.ad.core.model.service;

import java.util.UUID;

import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserRegForm;



public interface UserService extends pl.styall.library.core.model.defaultimpl.UserService {
	public User register(UserRegForm userRegForm);
	public boolean chechMailExists(String mail);
	public boolean changePassword(String id, String oldPassword, String newPassword);
//	public void addAddress(UUID userId, Address address);
}
