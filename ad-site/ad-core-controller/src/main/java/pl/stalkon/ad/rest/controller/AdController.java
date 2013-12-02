package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.Tag;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.Contest.State;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.AdsMapWrapper;
import pl.stalkon.ad.core.model.dto.AutocompleteDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.extensions.AjaxNotLoggedInException;
import pl.stalkon.video.api.service.VideoApiException;
import pl.stalkon.video.api.service.VideoApiService;
import pl.styall.library.core.rest.ext.EntityDtmMapper;
import pl.styall.library.core.rest.ext.MultipleObjectResponse;
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
				false);
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
		System.out.println(tags.size());
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

}
