package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.AdData;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;
import pl.stalkon.dailymotion.api.module.service.UploadStatus;
import pl.styall.library.core.ext.QueryObject;
import pl.styall.library.core.ext.QueryObjectWrapper;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;

@Controller
public class AdController extends BaseController {
	private static final Logger logger = Logger.getLogger(AdController.class);

	@Autowired
	private AdService adService;

	@Autowired
	private DailymotionService dailyService;

	@RequestMapping(value = "ad/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody Ad ad, Principal principal)
			throws ValidationException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		adService.register(ad, socialLoggedUser.getId());
		return ad.getId();
	}

	@RequestMapping(value = "brand/{brandId}/ad/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody Ad ad, @PathVariable Long brandId,
			Principal principal) throws ValidationException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		adService.register(ad, socialLoggedUser.getId(), brandId);
		return ad.getId();
	}
	
	@RequestMapping(value = "ad/{adId}/comment", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody AdComment comment, @PathVariable("adId") Long adId,
			Principal principal) throws ValidationException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		adService.comment(adId, socialLoggedUser.getId(), comment);
		return comment.getId();
	}

	@RequestMapping(value = "ad/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public Ad get(@PathVariable("id") Long id) throws ValidationException {
		Ad ad = adService.get(id);
		return ad;
	}

	@RequestMapping(value = "ad/{adId}/rank", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public void vote(@PathVariable("adId") Long adId,
			@RequestParam("value") Short value, Principal principal)
			throws ValidationException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (value > 10 || value < 1) {
			throw new ValidationException("value", "rank");
		}
		adService.vote(adId, socialLoggedUser.getId(), value);
	}

	@RequestMapping(value = "video/ad", method = RequestMethod.GET)
	@ResponseBody
	public UploadStatus getUploadUrl(HttpServletRequest request)
			throws DailymotionException {
		UploadStatus status = dailyService.getStatus();
		if (status == null) {
			throw new DailymotionException("Exception with dailymotion");
		}
		return status;
	}
	

	@RequestMapping(value = "brand/{brandId}/video/ad", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long addVideoAd(@Valid @RequestBody AdPostDto adPostDto, @PathVariable("brandId") Long brandId,
			Principal principal) throws ValidationException, DailymotionException {
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", adPostDto.getTitle());
		map.put("description", adPostDto.getDescription());
		String id = dailyService.createMedia(adPostDto.getUrl(), map);
		if(id == null){
			throw new DailymotionException("Exception with dailymotion"); 
		}
		Ad ad = new Ad();
		ad.setDailymotionId(id);
		AdData adData = new AdData();
		adData.setDescription(adPostDto.getDescription());
		adData.setTitle(adPostDto.getTitle());
		ad.setType(Ad.Type.MOVIE);
		ad.setAdData(adData);
		adService.register(ad, socialLoggedUser.getId(), brandId);
		return ad.getId();
	}
	
	@RequestMapping(value = "**/ad/", method = RequestMethod.GET)
	@ResponseBody
	public List<Ad> get(
			@QueryObject(objects = { "user", "brand" }) QueryObjectWrapper queryObjectWrapper)
			throws ValidationException {
		List<Ad> ads = adService.get(queryObjectWrapper.queryObject);
		return ads;
	}
	// @RequestMapping(value="/password", method = RequestMethod.PUT, headers =
	// "Accept=application/json")
	// @ResponseStatus(HttpStatus.OK)
	// public void changePassword(@Valid @RequestBody ChangePasswordForm
	// changePasswordForm, Principal principal)throws ValidationException{
	// SocialLoggedUser SocialLoggedUser = (SocialLoggedUser) ((Authentication)
	// principal).getPrincipal();
	// if(!userService.changePassword(SocialLoggedUser.getId(),
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

}