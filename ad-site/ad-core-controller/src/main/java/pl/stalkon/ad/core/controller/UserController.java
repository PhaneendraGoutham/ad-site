package pl.stalkon.ad.core.controller;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.UserService;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	// @RequestMapping(value = "/", method = RequestMethod.POST, headers =
	// "Accept=application/json")
	// @ResponseBody
	// @ResponseStatus(HttpStatus.CREATED)
	// public Long add(@Valid @RequestBody UserRegForm userRegForm) throws
	// ValidationException {
	// if (userService.chechMailExists(userRegForm.getMail())) {
	// throw new ValidationException("mail", "NotUniqueMail");
	// }
	// User user = userService.register(userRegForm);
	// return user.getId();
	// }

	@RequestMapping(value = "/user/login", method = RequestMethod.GET)
	public String loginSite(Model model) {
		model.addAttribute("path", "user/login");
		return "user/login";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.GET)
	public String registerSite(Model model) {
		model.addAttribute("userRegForm", new UserRegForm());
		return "user/register";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
	public String processRegister(
			@Valid @ModelAttribute("userRegForm") UserRegForm userRegForm,
			BindingResult result) {
		 if (userService.chechMailExists(userRegForm.getMail())) {
			 result.addError(new ObjectError("mail", "Uzytkownik zajety"));
		 }
		 if(result.hasErrors()){
			 return "user/register";
		 }
		 userService.register(userRegForm);
		 return "user/login";
	}

	// @RequestMapping(value="/password", method = RequestMethod.PUT, headers =
	// "Accept=application/json")
	// @ResponseStatus(HttpStatus.OK)
	// public void changePassword(@Valid @RequestBody ChangePasswordForm
	// changePasswordForm, Principal principal)throws ValidationException{
	// LoggedUser loggedUser = (LoggedUser) ((Authentication)
	// principal).getPrincipal();
	// if(!userService.changePassword(loggedUser.getId(),
	// changePasswordForm.getOldPassword(),
	// changePasswordForm.getNewPassword()))
	// throw new ValidationException("oldPassword", "WrongPassword");
	// }

	// @RequestMapping(value = "{userId}/address", method = RequestMethod.POST,
	// headers = "Accept=application/json")
	// @ResponseBody
	// @ResponseStatus(HttpStatus.CREATED)
	// public UUID add(@Valid @RequestBody Address address,
	// @PathVariable("userId") UUID userId) throws ValidationException {
	// userService.addAddress(userId, address);
	// return address.getId();
	// }

	@RequestMapping("/test")
	public String test() {
		return "test";
	}

}