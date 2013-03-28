package pl.stalkon.ad.core.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.DailymotionData;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.core.security.SocialLoggedUser.LoggedType;
import pl.stalkon.ad.social.facebook.SiteFacebookIntegrator;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;

@Controller
public class AdController {

	@Autowired
	private AdService adService;

	@Autowired
	private DailymotionService dailyService;

	@Autowired
	private SiteFacebookIntegrator siteFacebookIntegrator;

	@Autowired
	private Environment env;

	@RequestMapping(value = "ad/register")
	@Secured(value = "isAuthenticated()")
	public String getAddPage(Model model) throws DailymotionException {
		AdPostDto adDto = new AdPostDto();
		model.addAttribute("adPostDto", adDto);
		model.addAttribute("dailymotionUploadUrl", dailyService.getStatus()
				.getUrl());
		model.addAttribute("path", "ad/register");
		return "ad/register";
	}

	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	@Secured(value = "isAuthenticated()")
	public String add(@Valid @ModelAttribute("adPostDto") AdPostDto adPostDto,
			BindingResult result, Principal principal)
			throws DailymotionException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			return "ad/register";
		}
		DailymotionData dailymotionData = dailyService.createMedia(
				adPostDto.getUrl(), adPostDto.getTitle());
		Ad ad = adService.register(adPostDto, dailymotionData,
				socialLoggedUser.getId());
//		if (socialLoggedUser.getType() == LoggedType.SOCIAL)
//			siteFacebookIntegrator.notifyOnAdCreated(env.getProperty("app.domain")
//					+ "/ad/" + ad.getId(), ad.getTitle(), ad.getDescription());
		return "redirect:browser";
	}

	@RequestMapping(value = {"ad/main", "/"}, method = RequestMethod.GET)
	public String mainSite(Model model) {
		AdBrowserWrapper adBrowserWrapper = adService.getMain(0, 20);
		model.addAttribute("adBrowserWrapper", adBrowserWrapper);
		model.addAttribute("path", "ad/main");
		return "browser";
	}

	@RequestMapping(value = "ad/update/{id}", method = RequestMethod.GET)
	@Secured("isAuthenticated()")
	public String getUpdatePage(Model model, Principal principal,
			@PathVariable("id") Long adId) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (!adService.isOwner(adId, socialLoggedUser.getId())) {
			return "/denied";
		}
		AdPostDto adDto = new AdPostDto();
		model.addAttribute("adPostDto", adDto);
		return "ad/update";
	}

	@RequestMapping(value = "ad/{id}", method = RequestMethod.PUT)
	@Secured("isAuthenticated()")
	public String update(
			@Valid @ModelAttribute("adPostDto") AdPostDto adPostDto,
			Principal principal, @PathVariable("id") Long adId) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Ad ad = adService.update(adPostDto, adId, socialLoggedUser.getId());
		if (ad == null) {
			return "/denied";
		}
		return "ad/update";
	}

	@RequestMapping(value = "ad/{id}", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
	public boolean change(
			@PathVariable("id") Long id,
			@RequestParam(required = false, value = "approved") Boolean approved,
			@RequestParam(required = false, value = "ageProtected") Boolean ageProtected,
			@RequestParam(required = false, value = "onMain") Boolean onMain) {
		if (approved != null) {
			adService.changeApproval(id, approved);
		}
		if (ageProtected != null) {
			adService.changeAgeProtected(id, ageProtected);
		}
		if (onMain != null) {
			adService.putOnMain(id);
		}
		return true;
	}

}
