package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAnswer;
import pl.stalkon.ad.core.model.Contest.State;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.UserInfoService;
import pl.stalkon.ad.core.model.service.UserService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.extensions.AlreadyPostedException;
import pl.stalkon.ad.extensions.ContestFinishedException;
import pl.styall.library.core.rest.ext.SingleObjectResponse;

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

	@RequestMapping(value = "/brand/{brandId}/contest", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isUserBrandOwner(principal.id, #brandId)")
	@ResponseBody
	public SingleObjectResponse register(@Valid @RequestBody ContestPostDto contestPostDto,
			Principal principal, @PathVariable("brandId") Long brandId) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		contestPostDto.setBrandId(brandId);
		Contest contest = contestService.register(socialLoggedUser.getId(),
				contestPostDto);
		return new SingleObjectResponse(contest.getId());
	}

	@RequestMapping(value = "contest/{contestId}", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	@ResponseBody
	public void update(@Valid @RequestBody ContestPostDto contestPostDto,
			@PathVariable("contestId") Long contestId) {
		contestService.upadate(contestId, contestPostDto);
	}

	@RequestMapping(value = "contest", method = RequestMethod.GET)
	@ResponseBody
	public ContestBrowserWrapper get(
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestBrowserWrapper contestBrowserWrapper = contestService
				.get(new Paging(page, CONTESTS_PER_PAGE));
		return contestBrowserWrapper;
	}

	@RequestMapping(value = "brand/{brandId}/contest", method = RequestMethod.GET)
	@ResponseBody
	public ContestBrowserWrapper getByBrand(
			@PathVariable("brandId") Long brandId,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestBrowserWrapper contestBrowserWrapper = contestService
				.getByBrand(brandId, new Paging(page, CONTESTS_PER_PAGE));
		return contestBrowserWrapper;
	}

	@RequestMapping(value = "contest/{contestId}", method = RequestMethod.GET)
	@ResponseBody
	public Contest get(@PathVariable("contestId") Long contestId,
			Principal principal, HttpServletRequest request) {
		Contest contest = contestService.get(contestId);
		return contest;
	}

	@RequestMapping(value = "contest/{contestId}/answer", method = RequestMethod.GET)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	@ResponseBody
	public ContestAnswerBrowserWrapper getContestAnswers(
			@PathVariable("contestId") Long contestId,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page) {
		ContestAnswerBrowserWrapper answerBrowserWrapper = contestService
				.getContestAnswers(contestId, new Paging(page,
						CONTESTS_ANSWERS_PER_PAGE));
		return answerBrowserWrapper;
	}

	@RequestMapping(value = "contest/{contestId}/ad/{contestAdId}/state", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	@ResponseBody
	public void changeContestAdState(@PathVariable("contestId") Long contestId,
			@PathVariable("contestAdId") Long contestAdId,
			@RequestParam(value = "winner", required = true) Boolean winner) {
		contestService.changeAdWinnerState(contestAdId, winner);
	}

	@RequestMapping(value = "contest/{contestId}/answer/{contestAnswerId}/state", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	@ResponseBody
	public void changeContestAnswerState(
			@PathVariable("contestId") Long contestId,
			@PathVariable("contestAnswerId") Long contestAnswerId,
			@RequestParam(value = "winner", required = true) Boolean winner) {
		contestService.changeAnswerWinnerState(contestAnswerId, winner);
	}

	@RequestMapping(value = "contest/{contestId}/answer", method = RequestMethod.POST)
	@ResponseBody
	public SingleObjectResponse registerAnswer(@PathVariable("contestId") Long contestId,
			@RequestParam("answer") String answer, Principal principal)
			throws ContestFinishedException, AlreadyPostedException {
		Contest contest = contestService.get(contestId);
		if (contest.getState() == State.FINISHED) {
			throw new ContestFinishedException();
		}
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (contestService.hasUserPostedAnswer(socialLoggedUser.getId(),
				contestId)) {
			throw new AlreadyPostedException();
		}

		ContestAnswer contestAnswer = contestService.registerAnswer(
				socialLoggedUser.getId(), contestId, answer);
		return new SingleObjectResponse(contestAnswer.getId());
	}

	@RequestMapping(value = "contest/{contestId}/score", method = RequestMethod.GET)
	@PreAuthorize("@controllerHelperBean.isContestAdmin(principal.id, #contestId)")
	public void changeContestAnswerState(
			@PathVariable("contestId") Long contestId) {
		contestService.score(contestId);
		AdSearchDto adSearchDto = new AdSearchDto();
		adSearchDto.setContestId(contestId);
		adSearchDto.setWinner(true);
		List<Ad> ads = adService.getList(adSearchDto, new Paging(), true);
		contestService.setWinnerUserInfo(ads, contestId);
	}

	// @RequestMapping(value = "contest/message/{userInfoId}", method =
	// RequestMethod.GET)
	// public String getContestMessage(
	// @PathVariable("userInfoId") Long userInfoId, Principal principal) {
	// SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication)
	// principal)
	// .getPrincipal();
	// UserInfo userInfo = userInfoService.getWithContest(userInfoId);
	// User user = userService.getWithAddresses(socialLoggedUser.getId());
	// model.addAttribute("user", user);
	// UserAddressDto userAddressDto = new UserAddressDto();
	// if (user.getAddresses().size() > 0) {
	// model.addAttribute("address", user.getAddresses().get(0));
	// userAddressDto.setAddress(user.getAddresses().get(0));
	// userAddressDto.setFirstname(user.getUserData().getName());
	// userAddressDto.setSurname(user.getUserData().getSurname());
	// }
	// model.addAttribute("userInfo", userInfo);
	// model.addAttribute("userAddressDto", userAddressDto);
	// return "contest/contest-winner-info";
	// }

//	@RequestMapping(value = "contest/message/{userInfoId}/accept", method = RequestMethod.GET)
//	public void acceptUserContestInfo(
//			@PathVariable("userInfoId") Long userInfoId) {
//		userInfoService.setHandled(userInfoId, true,
//				UserInfo.Type.CONTEST_WINNER);
//	}

}
