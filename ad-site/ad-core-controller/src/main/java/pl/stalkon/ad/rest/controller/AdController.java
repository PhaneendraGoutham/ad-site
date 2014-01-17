package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.stalkon.ad.core.model.Tag;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.AdsMapWrapper;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.video.api.service.VideoApiException;
import pl.stalkon.video.api.service.VideoApiService;
import pl.styall.library.core.rest.ext.EntityDtmMapper;
import pl.styall.library.core.rest.ext.SingleObjectResponse;

@Controller
public class AdController {

	public final int AD_PER_PAGE = 5;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

	@Autowired
	private EntityDtmMapper entityDtmMapper;

	@Autowired
	private VideoApiService videoApiService;

	@Autowired
	private AdService adService;
	
	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/ad", method = RequestMethod.POST)
	@ResponseBody
	public SingleObjectResponse add(@Valid @RequestBody AdPostDto adPostDto,
			Principal principal) throws VideoApiException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Ad ad = videoApiService.setVideoDetails(adPostDto);
		Ad result = adService.register(adPostDto, ad, socialLoggedUser.getId(),
				false);
		videoApiService.setApiData(ad);
		return new SingleObjectResponse(result.getId());
	}
	
	@RequestMapping(value = "/brand/{brandId}/ad", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isUserBrandOwner(principal.id, #adPostDto.brandId)")
	@ResponseBody
	public SingleObjectResponse addBrandAd(@Valid @RequestBody AdPostDto adPostDto,
			Principal principal, @PathVariable("brandId") Long brandId) throws VideoApiException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Ad ad = videoApiService.setVideoDetails(adPostDto);
		Ad result = adService.register(adPostDto, ad, socialLoggedUser.getId(),
				true);
		videoApiService.setApiData(ad);
		return new SingleObjectResponse(result.getId());
	}

	@RequestMapping(value = { "ad", "user/{userId}/ad",
			"contest/{contestId}/ad", "brand/{brandId}/ad" }, method = RequestMethod.GET)
	@ResponseBody
	public AdsMapWrapper findAllBy(AdSearchDto adSearchDto, Principal principal) {
		AdBrowserWrapper adBrowserWrapper = getAds(adSearchDto, principal);
		return new AdsMapWrapper(adBrowserWrapper, entityDtmMapper);
	}

	@RequestMapping(value = "ad/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get(@PathVariable("id") Long id,
			Principal principal) {
		Ad ad = adService.get(id, controllerHelperBean.getActive(principal));
//		if(ad != null)
//		System.out.println(ad.getParent().getId());
		return entityDtmMapper.mapEntityToDtm(ad, Ad.class, Ad.JSON_SHOW);
	}

	@RequestMapping(value = "ad/rand", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> get() {
		Ad ad = adService.rand();
		return entityDtmMapper.mapEntityToDtm(ad, Ad.class, Ad.JSON_SHOW);
	}

	@RequestMapping(value = "tag", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getTags() {
		List<Tag> tags = adService.getTags();
		return entityDtmMapper.mapEntitiesToDtm(tags, Tag.class,
				Tag.JSON_SM_SHOW);
	}
	
	@RequestMapping(value = "ad/{id}/rate", method = RequestMethod.POST)
	@ResponseBody
	public void vote(@PathVariable(value = "id") Long id,
			@RequestParam(value="rank") Short rank, Principal principal){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		adService.vote(id, socialLoggedUser.getId(), rank);
	}

	private AdBrowserWrapper getAds(AdSearchDto adSearchDto, Principal principal) {
		AdBrowserWrapper adBrowserWrapper = adService.get(adSearchDto,
				new Paging(adSearchDto.getPage(), AD_PER_PAGE),
				controllerHelperBean.getActive(principal));
		return adBrowserWrapper;
	}
	
	@RequestMapping(value = "ad/{id}/state", method = RequestMethod.POST)
	@ResponseBody
	public void update(
			@PathVariable("id") Long adId,
			@RequestParam(value = "place", required = false) Short place,
			@RequestParam(value = "approved", required = false) Boolean approved,
			@RequestParam(value = "ageProtected", required = false) Boolean ageProtected) {
		if (place != null) {
			adService.changePlace(adId, Place.values()[place]);
		}
		if (approved != null) {
			adService.changeApproval(adId, approved);
		}
		if (ageProtected != null) {
			adService.changeAgeProtected(adId, ageProtected);
		}
	}
	
	@RequestMapping(value="ad/{id}/report", method=RequestMethod.POST)
	@ResponseBody
	public void reportAdAbuse(@PathVariable("id") Long adId, @RequestParam("message") String message){
		mailService.sendAdAbuseMessage(adId, message);
	}

}
