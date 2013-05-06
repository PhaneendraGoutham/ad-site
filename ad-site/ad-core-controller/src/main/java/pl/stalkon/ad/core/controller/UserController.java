package pl.stalkon.ad.core.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dto.ChangePasswordDto;
import pl.stalkon.ad.core.model.dto.UserProfileDto;
import pl.stalkon.ad.core.model.dto.UserRegForm;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.core.model.service.impl.MailServiceImpl;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class UserController extends BaseController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private MailService mailService;
	
	@Autowired
	private MessageSource messageSource;
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
		fileService.validateFile(result, userRegForm.getAvatarFile(),
				"avatarFile");
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
			
		}

		User user = userService.register(userRegForm);
		String baseUrl = String.format("%s://%s:%d%s/", request.getScheme(),
				request.getServerName(), request.getServerPort(),
				request.getContextPath());
		if (!userRegForm.getAvatarFile().isEmpty()) {
			String avatarPath = fileService.getPath("users", "avatar", null,
					user.getId().toString());
			try {
				String ext = fileService.saveFile(avatarPath,
						userRegForm.getAvatarFile(), 43, 43);
				userService.setUserThumbnail(baseUrl
						+ "resources/users/avatar-" + user.getId() + "." + ext,
						user.getId());
			} catch (IOException e) {
			}
		} else {
			userService.setUserThumbnail(baseUrl + "resources/img/no-user.gif",
					user.getId());
		}

//		 mailService.sendUserVerificationEmail(user); TODO:
		redirectAttributes
				.addFlashAttribute(
						"info",
						messageSource.getMessage("info.user.registered", null, LocaleContextHolder.getLocale()));
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
		redirectAttributes.addFlashAttribute("info",messageSource.getMessage("info.user.password.changed", null, LocaleContextHolder.getLocale()));
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
	public String activate(@PathVariable("token") String token, RedirectAttributes redirectAttributes) {
		boolean activated = userService.activate(token);
		if (activated) {
			redirectAttributes
					.addFlashAttribute("info",messageSource.getMessage("info.user.activated", null, LocaleContextHolder.getLocale()));
			return "redirect:/info-page";
		}
		return "redirect:/";
	}


}