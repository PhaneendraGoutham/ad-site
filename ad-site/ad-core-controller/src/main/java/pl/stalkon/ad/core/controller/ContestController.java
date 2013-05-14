package pl.stalkon.ad.core.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.HttpRequest;
import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.Contest.State;
import pl.stalkon.ad.core.model.Contest.Type;
import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;
import pl.stalkon.ad.core.model.dto.UserAddressDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.UserInfoService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.styall.library.core.security.filter.UserMessageSessionAttribute;

@Controller
public class ContestController {
	public final int CONTESTS_PER_PAGE = 5;
	public final int CONTESTS_ANSWERS_PER_PAGE = 10;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private AdService adService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "brand/{brandId}/contest/register", method = RequestMethod.GET)
	public String getRegistrationPage(@PathVariable("brandId") Long brandId,
			Model model, Principal principal, HttpServletRequest request) {
		if (!controllerHelperBean.isUserBrandOwner(request, principal, brandId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		ContestPostDto contestPostDto = new ContestPostDto();
		contestPostDto.setBrandId(brandId);
		model.addAttribute("contestPostDto", contestPostDto);
		model.addAttribute("actionUrl", "/contest");
		return "contest/register";
	}

	@RequestMapping(value = "contest/{contestId}/edit", method = RequestMethod.GET)
	public String getEditPage(@PathVariable("contestId") Long contestId,
			Model model, Principal principal, HttpServletRequest request) {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		Contest contest = contestService.get(contestId);
		ContestPostDto contestPostDto = new ContestPostDto(contest);
		model.addAttribute("contestPostDto", contestPostDto);
		model.addAttribute("path", "contest/edit");
		model.addAttribute("contest", contest);
		model.addAttribute("actionUrl", "/contest/" + contest.getId());
		return "contest/register";
	}

	@RequestMapping(value = "contest", method = RequestMethod.POST)
	public String register(
			@Valid @ModelAttribute("contestPostDto") ContestPostDto contestPostDto,
			BindingResult result, Principal principal,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		return saveOrUpdateContest(contestPostDto, result, principal, request,
				redirectAttributes, null);
	}

	@RequestMapping(value = "contest/{contestId}", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("contestPostDto") ContestPostDto contestPostDto,
			BindingResult result, Principal principal,
			HttpServletRequest request, RedirectAttributes redirectAttributes,
			@PathVariable("contestId") Long contestId) {
		return saveOrUpdateContest(contestPostDto, result, principal, request,
				redirectAttributes, contestId);
	}

	private String saveOrUpdateContest(ContestPostDto contestPostDto,
			BindingResult result, Principal principal,
			HttpServletRequest request, RedirectAttributes redirectAttributes,
			Long contestId) {
		if (result.hasErrors()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		Contest contest = null;
		if (contestId == null) {
			if (!controllerHelperBean.isUserBrandOwner(request, principal,
					contestPostDto.getBrandId())) {
				controllerHelperBean.throwAccessDeniedException(request);
			}
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			contest = contestService.register(socialLoggedUser.getId(),
					contestPostDto);
			controllerHelperBean.reathenticateUser(socialLoggedUser
					.getUsername());
			redirectAttributes.addFlashAttribute("info", messageSource
					.getMessage("info.contest.registered", null,
							LocaleContextHolder.getLocale()));
		} else {
			if (!controllerHelperBean.isContestAdmin(request, principal,
					contestId)) {
				controllerHelperBean.throwAccessDeniedException(request);
			}
			contest = contestService.upadate(contestId, contestPostDto);
		}
		return "redirect:/contest/" + contest.getId() + "/edit";
	}

	@RequestMapping(value = "contest", method = RequestMethod.GET)
	public String get(
			Model model,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestBrowserWrapper contestBrowserWrapper = contestService
				.get(new Paging(page, CONTESTS_PER_PAGE));
		controllerHelperBean.preparePagination(CONTESTS_PER_PAGE, model,
				contestBrowserWrapper.getTotal(), page);
		model.addAttribute("contests", contestBrowserWrapper.getContests());
		model.addAttribute("path", "contest/contests-list");
		return "contest/contests-list";
	}

	@RequestMapping(value = "brand/{brandId}/contest", method = RequestMethod.GET)
	public String getByBrand(
			@PathVariable("brandId") Long brandId,
			Model model,
			HttpServletRequest request,
			Principal principal,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestBrowserWrapper contestBrowserWrapper = contestService
				.getByBrand(brandId, new Paging(page, CONTESTS_PER_PAGE));
		controllerHelperBean.preparePagination(CONTESTS_PER_PAGE, model,
				contestBrowserWrapper.getTotal(), page);
		model.addAttribute("contests", contestBrowserWrapper.getContests());
		model.addAttribute(
				"brandAdmin",
				controllerHelperBean.isUserBrandOwner(request, principal,
						brandId)
						|| request.isUserInRole(UserRoleDef.ROLE_ADMIN));
		model.addAttribute("brandId", brandId);
		return "contest/contests-list";
	}

	@RequestMapping(value = "contest/{contestId}", method = RequestMethod.GET)
	public String get(Model model, @PathVariable("contestId") Long contestId,
			Principal principal, HttpServletRequest request) {
		Contest contest = contestService.get(contestId);
		// if (contest.getState() == State.FINISHED) {
		// // if (contest.getType() == Type.AD) {
		// // contest = contestService.getWithContestAds(contestId);
		// // } else {
		// // contest = contestService.getWithAnswers(contestId);
		// // }
		// //TODO: get winners
		//
		// } else
		if (principal != null) {
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			Boolean hasPosted = false;
			if (contest.getType() == Type.AD) {
				hasPosted = contestService.hasUserPostedAd(
						socialLoggedUser.getId(), contestId);
			} else {
				hasPosted = contestService.hasUserPostedAnswer(
						socialLoggedUser.getId(), contestId);
			}
			model.addAttribute("hasPosted", hasPosted);

		}
		if (controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			model.addAttribute("contestAdmin", true);
		}
		model.addAttribute("contest", contest);
		return "contest";
	}

	@RequestMapping(value = "contest/{contestId}/answer", method = RequestMethod.GET)
	public String getContestAnswers(
			Model model,
			@PathVariable("contestId") Long contestId,
			Principal principal,
			HttpServletRequest request,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		ContestAnswerBrowserWrapper answerBrowserWrapper = contestService
				.getContestAnswers(contestId, new Paging(page,
						CONTESTS_ANSWERS_PER_PAGE));
		controllerHelperBean.preparePagination(CONTESTS_PER_PAGE, model,
				answerBrowserWrapper.getTotal(), page);
		model.addAttribute("answers", answerBrowserWrapper.getAnswers());
		model.addAttribute("contestId", contestId);
		return "contest/contest-answer-list";
	}

	@RequestMapping(value = "contest/{contestId}/ad/{contestAdId}/state", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void changeContestAdState(@PathVariable("contestId") Long contestId,
			@PathVariable("contestAdId") Long contestAdId,
			@RequestParam(value = "winner", required = true) Boolean winner,
			HttpServletRequest request, Principal principal) {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		contestService.changeAdWinnerState(contestAdId, winner);
	}

	@RequestMapping(value = "contest/{contestId}/answer/{contestAnswerId}/state", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void changeContestAnswerState(
			@PathVariable("contestId") Long contestId,
			@PathVariable("contestAnswerId") Long contestAnswerId,
			@RequestParam(value = "winner", required = true) Boolean winner,
			HttpServletRequest request, Principal principal) {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		contestService.changeAnswerWinnerState(contestAnswerId, winner);
	}

	@RequestMapping(value = "contest/{contestId}/answer", method = RequestMethod.POST)
	public String registerAnswer(@PathVariable("contestId") Long contestId,
			@RequestParam("answer") String answer, Principal principal,
			RedirectAttributes redirectAttributes) {
		if (answer == null) {
			return "contest/" + contestId;
		}
		Contest contest = contestService.get(contestId);
		if (contest.getState() == State.FINISHED) {
			redirectAttributes.addFlashAttribute("info",
					"Konkurs już się zakończył");
			return "redirect:/info-page";
		}
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (contestService.hasUserPostedAnswer(socialLoggedUser.getId(),
				contestId)) {
			redirectAttributes.addFlashAttribute("info", "Już odpowiedziałeś");
			return "redirect:/info-page";
		}

		contestService.registerAnswer(socialLoggedUser.getId(), contestId,
				answer);
		redirectAttributes.addFlashAttribute("info", messageSource.getMessage(
				"info.contest.answer.post", null,
				LocaleContextHolder.getLocale()));
		return "redirect:/info-page";
	}

	@RequestMapping(value = "contest/{contestId}/score", method = RequestMethod.GET)
	public String changeContestAnswerState(
			@PathVariable("contestId") Long contestId, Principal principal,
			HttpServletRequest request) {
		if (!controllerHelperBean.isContestAdmin(request, principal, contestId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		contestService.score(contestId);
		AdSearchDto adSearchDto = new AdSearchDto();
		adSearchDto.setContestId(contestId);
		adSearchDto.setWinner(true);
		List<Ad> ads = adService.getList(adSearchDto, new Paging(), true);
		contestService.setWinnerUserInfo(ads, contestId);
		return "redirect:/contest/" + contestId;
	}

	@RequestMapping(value = "contest/message/{userInfoId}", method = RequestMethod.GET)
	public String getContestMessage(
			@PathVariable("userInfoId") Long userInfoId, Principal principal,
			HttpServletRequest request, Model model) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		UserInfo userInfo = userInfoService.getWithContest(userInfoId);
		User user = userService.getWithAddresses(socialLoggedUser.getId());
		model.addAttribute("user", user);
		UserAddressDto userAddressDto = new UserAddressDto();
		if (user.getAddresses().size() > 0) {
			model.addAttribute("address", user.getAddresses().get(0));
			userAddressDto.setAddress(user.getAddresses().get(0));
			userAddressDto.setFirstname(user.getUserData().getName());
			userAddressDto.setSurname(user.getUserData().getSurname());
		}
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("userAddressDto", userAddressDto);
		return "contest/contest-winner-info";
	}

	@RequestMapping(value = "contest/message/{userInfoId}/accept", method = RequestMethod.GET)
	public String acceptUserContestInfo(
			@PathVariable("userInfoId") Long userInfoId, Principal principal,
			HttpServletRequest request) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		userInfoService.setHandled(userInfoId, true,
				UserInfo.Type.CONTEST_WINNER);
		controllerHelperBean.setUserMessagesSessionAttr(socialLoggedUser,
				request);
		return "redirect:/";
	}

}
