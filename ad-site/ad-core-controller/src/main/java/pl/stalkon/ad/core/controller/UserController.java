package pl.stalkon.ad.core.controller;

import java.util.UUID;

import javax.validation.Valid;

import net.dmcloud.cloudkey.CloudKey;
import net.dmcloud.util.DCObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserRegForm;
import pl.styall.library.core.model.defaultimpl.UserService;




@Controller
@RequestMapping("user")

public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);
	private static final String apiKey = "d09f4a20348b3b5a229fdada9cb183e0e0a0e38e";
	private static final String userId = "510fac5794a6f6702601b20b";
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody UserRegForm userRegForm) throws ValidationException {
		if (userService.chechMailExists(userRegForm.getMail())) {
			throw new ValidationException("mail", "NotUniqueMail");
		}
		User user = userService.register(userRegForm);
		return user.getId();
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
//	public UUID add(@Valid @RequestBody Address address,
//			@PathVariable("userId") UUID userId) throws ValidationException {
//		userService.addAddress(userId, address);
//		return address.getId();
//	}
	
	@RequestMapping("/test")
	public String test() {
		CloudKey cloudKey = new CloudKey(userId, apiKey);
		try {
			DCObject result = cloudKey.fileUpload(true, "?", "");
			logger.debug(result);
			logger.debug(result.pull("url"));
			logger.debug(result.pull("status"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "test";
	}

}