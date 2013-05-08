package pl.stalkon.ad.core.model.service;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dto.UserAddressDto;
import pl.stalkon.ad.core.model.dto.UserProfileDto;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.styall.library.core.model.service.AbstractUserService;

public interface UserService extends AbstractUserService<User> {
	public User register(UserRegForm userRegoForm);

	public void updateProfile(UserProfileDto userProfileDto, Long id);

	public void setUserThumbnail(String url, Long id);
	
	public User getWithAddresses(Long userId);
	
	public void updateUserAddress(UserAddressDto userAddressDto, Long userId);

}
