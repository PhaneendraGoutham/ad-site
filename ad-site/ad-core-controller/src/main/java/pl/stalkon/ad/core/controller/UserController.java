package pl.stalkon.ad.core.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sun.mail.imap.protocol.Status;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.dto.ChangePasswordDto;
import pl.stalkon.ad.core.model.dto.UserAddressDto;
import pl.stalkon.ad.core.model.dto.UserProfileDto;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.model.service.UserInfoService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.core.model.service.impl.MailServiceImpl;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.extensions.UploadingFileException;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private MailService mailService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

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
	public String loginSite(Model model, Principal principal) {
		if (principal != null && ((Authentication) principal).isAuthenticated())
			return "redirect:/";
		model.addAttribute("path", "user/login");
		return "user/login";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.GET)
	public String registerSite(Model model) {
		model.addAttribute("userRegForm", new UserRegForm());
		return "user/register";
	}

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String processRegister(
			@Valid @ModelAttribute("userRegForm") UserRegForm userRegForm,
			BindingResult result, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		if (userService.chechMailExists(userRegForm.getMail())) {
			result.addError(new ObjectError("mail", "Uzytkownik zajety"));
		}
		if (userService.chechUsernameExists(userRegForm.getUsername())) {
			result.addError(new ObjectError("username", "Uzytkownik zajety"));
		}
		if (result.hasErrors()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);

		}

		User user = userService.register(userRegForm);

		mailService.sendUserVerificationEmail(user); 
		redirectAttributes.addFlashAttribute("info", messageSource.getMessage(
				"info.user.registered", null, LocaleContextHolder.getLocale()));
		return "redirect:/info-page";
	}

	@RequestMapping(value = "/user/profile", method = RequestMethod.GET)
	public String getProfilePage(Model model, Principal principal) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		User user = userService.getInitialized(socialLoggedUser.getId());
		UserProfileDto userProfileDto = new UserProfileDto(user.getUserData()
				.getName(), user.getUserData().getSurname(), user
				.getCredentials().getMail(), user.getDisplayName());
		model.addAttribute("userProfileDto", userProfileDto);
		model.addAttribute("avatar", user.getUserData().getImageUrl());
		model.addAttribute("username", user.getCredentials().getUsername());
		model.addAttribute("mail", user.getCredentials().getMail());
		return "user/profile";
	}

	@RequestMapping(value = "/user/profile", method = RequestMethod.POST)
	public String updateProfile(
			@Valid @ModelAttribute("userProfileDto") UserProfileDto userProfileDto,
			BindingResult result, Principal principal,
			RedirectAttributes redirectAttributes) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		userService.updateProfile(userProfileDto, socialLoggedUser.getId());
		return "redirect:/user/profile";
	}

	@RequestMapping(value = "/user/password", method = RequestMethod.GET)
	public String getPasswordPage(Model model) {
		model.addAttribute("changePasswordDto", new ChangePasswordDto());
		return "user/password";
	}

	@RequestMapping(value = "/user/password", method = RequestMethod.POST)
	public String updatePassword(
			@Valid @ModelAttribute("changePasswordDto") ChangePasswordDto changePasswordDto,
			BindingResult result, Principal principal,
			RedirectAttributes redirectAttributes) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		userService.changePassword(socialLoggedUser.getId(),
				changePasswordDto.getOldPassword(),
				changePasswordDto.getPassword());
		redirectAttributes.addFlashAttribute("info", messageSource.getMessage(
				"info.user.password.changed", null,
				LocaleContextHolder.getLocale()));
		return "redirect:/info-page";
	}

	@RequestMapping(value = "/user/email", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkMailExists(@RequestParam("mail") String mail) {
		boolean exists = userService.chechMailExists(mail);
		return !exists;
	}

	@RequestMapping(value = "/user/username", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkUsernameExists(@RequestParam("username") String username) {
		boolean exists = userService.chechUsernameExists(username);
		return !exists;
	}

	@RequestMapping(value = "/user/activate/{token}", method = RequestMethod.GET)
	public String activate(@PathVariable("token") String token,
			RedirectAttributes redirectAttributes) {
		boolean activated = userService.activate(token);
		if (activated) {
			redirectAttributes.addFlashAttribute("info", messageSource
					.getMessage("info.user.activated", null,
							LocaleContextHolder.getLocale()));
			return "redirect:/info-page";
		}
		return "redirect:/";
	}

	@RequestMapping(value = "/user/message", method = RequestMethod.GET)
	public String getMessages(Principal principal, Model model) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		List<UserInfo> userInfos = userInfoService.getMessages(
				socialLoggedUser.getId(), null);
		model.addAttribute("userInfos", userInfos);
		return "user/info-contest-list";
	}

	@RequestMapping(value = "/user/address/update", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public UserAddressDto getMessages(
			Principal principal,
			@Valid @ModelAttribute("userAddressDto") UserAddressDto userAddressDto,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		userService.updateUserAddress(userAddressDto, socialLoggedUser.getId());
		return userAddressDto;
	}
	
	@RequestMapping(value = "/user/password/recall", method = RequestMethod.GET)
	public String getRecallPassword(Model model) {
		return "user/password/recall";
	}
	
	@RequestMapping(value = "/user/terms", method = RequestMethod.GET)
	public String getTerms(Model model) {
		model.addAttribute("info", "Jeszcze nie wymyśliłem regulaminu");
		return "info-page";
	}
	
	@RequestMapping(value = "/user/password/recall", method = RequestMethod.POST)
	public String recallPassword(@RequestParam("mail") String mail, RedirectAttributes redirectAttributes) {
		if(mail == null){
			controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		String newPassword = userService.generateAndSetNewPassword(mail);
		mailService.sendNewPassword(mail, newPassword);
		redirectAttributes.addFlashAttribute("info", messageSource
				.getMessage("info.user.newpassword", null,
						LocaleContextHolder.getLocale()));
		return "user/password/recall";
	}
	
	
	

}