package pl.stalkon.ad.core.controller;




import java.security.Principal;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.security.authentication.LoggedUser;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class AdController extends BaseController {
	private static final Logger logger = Logger.getLogger(AdController.class);

	@Autowired
	private AdService adService;
	
	@RequestMapping(value = "ad/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody Ad ad, Principal principal) throws ValidationException {
		LoggedUser loggedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
		adService.register(ad, loggedUser.getId());
		return ad.getId();
	}

//	@RequestMapping(value="/password", method = RequestMethod.PUT, headers = "Accept=application/json")
//	@ResponseStatus(HttpStatus.OK)
//	public void changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm, Principal principal)throws ValidationException{
//		LoggedUser loggedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
//		if(!userService.changePassword(loggedUser.getId(), changePasswordForm.getOldPassword(), changePasswordForm.getNewPassword()))
//			throw new ValidationException("oldPassword", "WrongPassword");
//	}

	
//	@RequestMapping(value = "{userId}/address", method = RequestMethod.POST, headers = "Accept=application/json")
//	@ResponseBody
//	@ResponseStatus(HttpStatus.CREATED)
//	public Long add(@Valid @RequestBody Address address,
//			@PathVariable("userId") Long userId) throws ValidationException {
//		userService.addAddress(userId, address);
//		return address.getId();
//	}

}