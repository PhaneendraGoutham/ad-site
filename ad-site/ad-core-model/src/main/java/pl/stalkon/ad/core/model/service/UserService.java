package pl.stalkon.ad.core.model.service;

import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserRegForm;



public interface UserService extends pl.styall.library.core.model.defaultimpl.UserService {
	public User register(UserRegForm userRegForm);
	public boolean chechMailExists(String mail);
	public boolean changePassword(Long id, String oldPassword, String newPassword);
//	public void addAddress(Long userId, Address address);
}
