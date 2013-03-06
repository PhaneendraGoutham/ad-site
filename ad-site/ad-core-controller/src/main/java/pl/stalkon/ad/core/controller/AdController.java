package pl.stalkon.ad.core.controller;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.dmcloud.cloudkey.Helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.AppConstants;
import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Ad.Type;
import pl.stalkon.ad.core.model.AdData;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.core.security.SocialLoggedUser.LoggedType;
import pl.stalkon.ad.social.facebook.SiteFacebookIntegrator;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;
import pl.stalkon.dailymotion.api.module.service.UploadStatus;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class AdController {

	@Autowired
	private AdService adService;

	@Autowired
	private DailymotionService dailyService;

	@Autowired
	private SiteFacebookIntegrator siteFacebookIntegrator;

	@RequestMapping(value = "ad/register")
	public String adRegistration(Model model) throws DailymotionException {
		AdPostDto adDto = new AdPostDto();
		adDto.setType(Type.MOVIE);
		model.addAttribute("adPostDto", adDto);
		model.addAttribute("dailymotionUploadUrl", dailyService.getStatus()
				.getUrl());
		return "ad/register";
	}

	@RequestMapping(value = "secure/ad", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute("adPostDto") AdPostDto adPostDto,
			BindingResult result, Principal principal)
			throws DailymotionException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			return "adRegistration";
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", adPostDto.getTitle());
		map.put("description", adPostDto.getDescription());
		String id = dailyService.createMedia(adPostDto.getUrl(), map);
		String url = dailyService.getEmbeddedUrl(id);
		Ad ad = new Ad();
		ad.setDailymotionId(id);
		ad.setDailymotionUrl(url);
		AdData adData = new AdData();
		adData.setDescription(adPostDto.getDescription());
		ad.setTitle(adPostDto.getTitle());
		ad.setType(adPostDto.getType());
		ad.setAdData(adData);
		adService
				.register(ad, socialLoggedUser.getId(), adPostDto.getBrandId());
		if (socialLoggedUser.getType() == LoggedType.SOCIAL)
			siteFacebookIntegrator.notifyOnAdCreated(AppConstants.AD_URL + "/"
					+ ad.getId(), ad.getTitle(), ad.getAdData()
					.getDescription());
		return "adRegistrationSuccess";
	}

	@RequestMapping(value = "secure/video/ad", method = RequestMethod.GET)
	@ResponseBody
	public UploadStatus getUploadUrl(HttpServletRequest request)
			throws DailymotionException {
		UploadStatus status;
		status = dailyService.getStatus();
		return status;
	}
	
	@RequestMapping(value="ad/main", method=RequestMethod.GET)
	public String mainSite(Model model){
		AdBrowserWrapper adBrowserWrapper = adService.getMain(0, 20);
		model.addAttribute("adBrowserWrapper", adBrowserWrapper);
		return "ad/browser";
	}

}
