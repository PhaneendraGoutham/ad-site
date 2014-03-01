package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dto.BrandPostDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.extensions.NotFoundException;
import pl.stalkon.video.api.service.impl.WistiaApiService;
import pl.stalkon.video.api.wistia.WistiaException;
import pl.stalkon.video.api.wistia.WistiaStats;
import pl.styall.library.core.rest.ext.EntityDtmMapper;
import pl.styall.library.core.rest.ext.SingleObjectResponse;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private AdService adService;
	
	@Autowired
	private ContestService contestService;

	@Autowired
	private WistiaApiService wistiaApiService;

	@Autowired
	private EntityDtmMapper entityDtmMapper;

	@RequestMapping(value = "brand/sm", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> getSmallList() {
		List<Brand> brands = brandService.get();
		return entityDtmMapper.mapEntitiesToDtm(brands, Brand.class,
				Brand.JSON_SM_SHOW);
	}
	
	@RequestMapping(value = "brand", method = RequestMethod.GET)
	@ResponseBody
	public List<Object> get() {
		List<Brand> brands = brandService.get();
		return entityDtmMapper.mapEntitiesToDtm(brands, Brand.class,
				Brand.JSON_SHOW);
	}
	
	@RequestMapping(value = { "brand/{brandId}/wistiaproject" }, method = RequestMethod.GET)
	@ResponseBody
	public SingleObjectResponse getBrandWistiaProject(@PathVariable("brandId") Long brandId) {
		Brand brand = brandService.get(brandId);
		return new SingleObjectResponse(brand.getWistiaProjectData().getHashedId());
	}

	@RequestMapping(value = { "brand/{brandId}" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getBrandAds(@PathVariable("brandId") Long brandId) throws NotFoundException {
		Brand brand = brandService.get(brandId);
		if(brand == null)
			throw new NotFoundException();
		Long adsCount = adService.getBrandAdsCount(brandId);
		Long contestsCount = contestService.getBrandContestCount(brandId);
		Map<String, Object> result = entityDtmMapper.mapEntityToDtm(brand, Brand.class,
				Brand.JSON_SHOW);
		result.put("adsCount", adsCount);
		result.put("contestsCount", contestsCount);
		return result;
	}

	@RequestMapping(value = "brand/{brandId}", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isUserBrandOwner(principal.id, #brandId)")
	@ResponseBody
	public void update(@PathVariable("brandId") Long brandId,
			@Valid @RequestBody BrandPostDto brandPostDto) {
		brandService.update(brandPostDto, brandId);
	}

//	@RequestMapping(value = "/brand", method = RequestMethod.POST)
//	@ResponseBody
//	public Long add(@Valid @RequestBody BrandPostDto brandPostDto) {
//
//		WistiaProjectData wistiaProjectData = wistiaApiService
//				.createWistiaProject(brandPostDto.getName());
//		Brand brand = brandService.register(brandPostDto, wistiaProjectData,
//				null);
//		return brand.getId();
//	}

	@RequestMapping(value = "/company/{companyId}/brand", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("@companyService.isCompanyOfUser(principal.id,#companyId)")
	public SingleObjectResponse addByCompany(@Valid @RequestBody BrandPostDto brandPostDto,
			Principal principal, @PathVariable("companyId") Long companyId) {
		WistiaProjectData wistiaProjectData = wistiaApiService
				.createWistiaProject(brandPostDto.getName());
		Brand brand = brandService.register(brandPostDto, wistiaProjectData,
				companyId);
		return new SingleObjectResponse(brand.getId());
	}
	
	@RequestMapping(value = "/company/{companyId}/brand", method = RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("@companyService.isCompanyOfUser(principal.id,#companyId)")
	public List<Object> getCompanyBrands(@PathVariable("companyId") Long companyId) {
		List<Brand> brands = brandService.getCompanyBrands(companyId);
		List<Object> result = new EntityDtmMapper().mapEntitiesToDtm(brands, Brand.class, Brand.JSON_SHOW);
		return result;
	}

	@RequestMapping(value = "brand/{brandId}/stats", method = RequestMethod.POST)
	@PreAuthorize("@controllerHelperBean.isUserBrandOwner(principal.id, #brandId)")
	@ResponseBody
	public WistiaStats getStatsAndCosts(
			@PathVariable("brandId") Long brandId, @RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate ) throws WistiaException {
		Brand brand = brandService.get(brandId);
//		Double cost = brandService.getBrandCurrentCost(brandId);
//		LocalDate monthBegin = new LocalDate().withDayOfMonth(1);
//		LocalDate monthEnd = new LocalDate().plusMonths(1).withDayOfMonth(1)
//				.minusDays(1);
		System.out.println(startDate);
		WistiaStats wistiaStats = null;
		try {
			wistiaStats = wistiaApiService.getProjectStats(brand
					.getWistiaProjectData().getHashedId(), startDate,
					endDate);
		} catch (HttpServerErrorException e) {
			throw new WistiaException();
		}
		return wistiaStats;
	}

}
