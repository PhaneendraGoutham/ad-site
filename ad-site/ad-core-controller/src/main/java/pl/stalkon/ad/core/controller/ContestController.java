package pl.stalkon.ad.core.controller;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.HttpRequest;
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

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.Contest.State;
import pl.stalkon.ad.core.model.Contest.Type;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.security.SocialLoggedUser;

@Controller
public class ContestController {

	private static final int PER_PAGE = 5;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;
	
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "brand/{brandId}/contest/register", method = RequestMethod.GET)
	public String getRegistrationPage(@PathVariable("brandId") Long brandId,
			Model model, Principal principal, HttpServletRequest request) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (!controllerHelperBean.isUserBrandOwner(socialLoggedUser.getId(),
				brandId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		ContestPostDto contestPostDto = new ContestPostDto();
		contestPostDto.setBrandId(brandId);
		model.addAttribute("contestPostDto", contestPostDto);
		return "contest/register";
	}

	@RequestMapping(value = "contest", method = RequestMethod.POST)
	public String register(
			@Valid @ModelAttribute("contestPostDto") ContestPostDto contestPostDto,
			BindingResult result, Principal principal,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		fileService.validateFile(result, contestPostDto.getImage(), "image");
		if (result.hasErrors() || contestPostDto.getImage().isEmpty()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		if (!controllerHelperBean.isUserBrandOwner(request, principal,
				contestPostDto.getBrandId())) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Contest contest = contestService.register(socialLoggedUser.getId(),
				contestPostDto);
		String baseUrl = String.format("%s://%s:%d%s/", request.getScheme(),
				request.getServerName(), request.getServerPort(),
				request.getContextPath());
		String imagePath = fileService.getPath("contests", "image", null,
				contest.getId().toString());
		try {
			String ext = fileService.saveFile(imagePath,
					contestPostDto.getImage(), 120, 120);
			contestService.setContestImage(
					baseUrl + "resources/contests/image-" + contest.getId()
							+ "." + ext, contest.getId());
		} catch (IOException e) {
		}
		return "redirect:/contest";
	}

	@RequestMapping(value = "contest", method = RequestMethod.GET)
	public String get(
			Model model,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestBrowserWrapper contestBrowserWrapper = contestService
				.get(controllerHelperBean.getFrom(
						ControllerHelperBean.CONTESTS_PER_PAGE, page), PER_PAGE);
		controllerHelperBean.preparePagination(
				ControllerHelperBean.CONTESTS_PER_PAGE, model,
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
				.getByBrand(brandId, controllerHelperBean.getFrom(
						ControllerHelperBean.CONTESTS_PER_PAGE, page), PER_PAGE);
		controllerHelperBean.preparePagination(
				ControllerHelperBean.CONTESTS_PER_PAGE, model,
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
			Principal principal) {
		Contest contest = contestService.get(contestId);
		if (contest.getState() == State.FINISHED) {
			if (contest.getType() == Type.AD) {
				// contest = contestService.getWithAds(contestId); TODO:
			} else {
				contest = contestService.getWithAnswers(contestId);
			}
		} else if (principal != null) {
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
				.getContestAnswers(contestId, controllerHelperBean.getFrom(
						ControllerHelperBean.CONTESTS_ANSWERS_PER_PAGE, page),
						PER_PAGE);
		controllerHelperBean.preparePagination(
				ControllerHelperBean.CONTESTS_PER_PAGE, model,
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
			redirectAttributes
					.addFlashAttribute("info",
							"Konkurs już się zakończył");
			return "redirect:/info-page";
		}
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		contestService.registerAnswer(socialLoggedUser.getId(), contestId,
				answer);
		redirectAttributes.addFlashAttribute("info",messageSource.getMessage("info.contest.answer.post", null, LocaleContextHolder.getLocale()));
		return "redirect:/info-page";
	}

}
