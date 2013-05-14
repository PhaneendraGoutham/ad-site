package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.BrandPostDto;
import pl.stalkon.ad.core.model.dto.BrandSearchDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.video.api.service.impl.WistiaApiService;
import pl.stalkon.video.api.wistia.WistiaStats;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private WistiaApiService wistiaApiService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AdService adService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "brand", method = RequestMethod.GET)
	@ResponseBody
	public List<BrandSearchDto> getBrandByTerm(@RequestParam("term") String term) {
		return brandService.getByTerm(term);
	}

	@RequestMapping(value = { "brand/{brandId}" }, method = RequestMethod.GET)
	public String getBrandAds(
			@PathVariable("brandId") Long brandId,
			Model model,
			@RequestParam(required = false, value = "page", defaultValue = "1") int page,
			Principal principal, HttpServletRequest request,
			@ModelAttribute("adSearchDto") AdSearchDto adSearchDto) {
		Brand brand = brandService.get(brandId);
		model.addAttribute("brand", brand);
		// adSearchDto.setBrandList(Arrays.asList(brandId));
		// AdBrowserWrapper adBrowserWrapper = adService.get(adSearchDto,
		// controllerHelperBean.getFrom(ControllerHelperBean.AD_PER_PAGE,
		// page), ControllerHelperBean.AD_PER_PAGE,
		// controllerHelperBean.getActive(principal));
		// controllerHelperBean.preparePagination(
		// ControllerHelperBean.AD_PER_PAGE, model,
		// adBrowserWrapper.getTotal(), page);
		// model.addAttribute("adBrowserWrapper", adBrowserWrapper);
		model.addAttribute(
				"brandAdmin",
				controllerHelperBean.isUserBrandOwner(request, principal,
						brandId)
						|| request.isUserInRole(UserRoleDef.ROLE_ADMIN));
		return "brand/brand-info";
	}

	@RequestMapping(value = "brand/register")
	public String getRegistrationPage(Model model) {
		BrandPostDto brandPostDto = new BrandPostDto();
		model.addAttribute("brandPostDto", brandPostDto);
		model.addAttribute("path", "brand/register");
		model.addAttribute("actionUrl", "brand");
		return "brand/register";
	}

	@RequestMapping(value = "brand/{brandId}/edit")
	public String getEditPage(Model model,
			@PathVariable("brandId") Long brandId, Principal principal,
			HttpServletRequest request) {
		if (!controllerHelperBean.isUserBrandOwner(request, principal, brandId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		Brand brand = brandService.get(brandId);
		BrandPostDto brandPostDto = new BrandPostDto();
		brandPostDto.setDescription(brand.getDescription());
		brandPostDto.setName(brand.getName());
		model.addAttribute("brandPostDto", brandPostDto);
		model.addAttribute("brand", brand);
		model.addAttribute("path", "brand/edit");
		model.addAttribute("actionUrl", "brand/" + brandId);
		return "brand/register";
	}

	@RequestMapping(value = "brand/{brandId}", method = RequestMethod.POST)
	public String update(Model model, @PathVariable("brandId") Long brandId,
			Principal principal,
			@Valid @ModelAttribute("brandPostDto") BrandPostDto brandPostDto,
			BindingResult result, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		// SocialLoggedUser socialLoggedUser = (SocialLoggedUser)
		// ((Authentication) principal)
		// .getPrincipal();
		// if(!controllerHelperBean.isUserBrandOwner(socialLoggedUser.getId(),
		// brandId)){
		// controllerHelperBean.throwAccessDeniedException(request);
		// }
		// Company company = companyService
		// .getCompanyWithBrandsByUser(socialLoggedUser.getId());
		// Brand mockBrand = new Brand();
		// mockBrand.setId(brandId);
		// if (!company.getBrands().contains(mockBrand))
		// controllerHelperBean.throwAccessDeniedException(request);
		return registerOrUpdateBrand(brandPostDto, result, null, brandId,
				request, redirectAttributes);
	}

	@RequestMapping(value = "company/{id}/brand/register")
	public String getRegistrationPageByCompany(Model model,
			@PathVariable("id") Long companyId, Principal principal,
			HttpServletRequest request) {
		BrandPostDto brandPostDto = new BrandPostDto();
		model.addAttribute("brandPostDto", brandPostDto);
		model.addAttribute("companyId", companyId);
		model.addAttribute("path", "brand/register");
		model.addAttribute("actionUrl", "company/" + companyId + "/brand");
		return "brand/register";
	}

	@RequestMapping(value = "/brand", method = RequestMethod.POST)
	public String add(
			@Valid @ModelAttribute("brandPostDto") BrandPostDto brandPostDto,
			BindingResult result, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		return registerOrUpdateBrand(brandPostDto, result, null, null, request,
				redirectAttributes);
	}

	@RequestMapping(value = "/company/{companyId}/brand", method = RequestMethod.POST)
	public String addByCompany(
			@Valid @ModelAttribute("brandPostDto") BrandPostDto brandPostDto,
			BindingResult result, Principal principal,
			RedirectAttributes redirectAttributes,
			@PathVariable("companyId") Long companyId,
			HttpServletRequest request) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		boolean companyOfUser = companyService.isCompanyOfUser(
				socialLoggedUser.getId(), companyId);
		if (!companyOfUser)
			controllerHelperBean.throwAccessDeniedException(request);
		return registerOrUpdateBrand(brandPostDto, result, companyId, null,
				request, redirectAttributes);
	}

	private String registerOrUpdateBrand(BrandPostDto brandPostDto,
			BindingResult result, Long companyId, Long brandId,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		Brand brand = null;
		if (brandId == null) {
			WistiaProjectData wistiaProjectData = wistiaApiService
					.createWistiaProject(brandPostDto.getName());
			brand = brandService.register(brandPostDto, wistiaProjectData,
					companyId);
		} else {
			brand = brandService.update(brandPostDto, brandId);
		}

		if (brandId == null) {
			redirectAttributes.addFlashAttribute("info", messageSource
					.getMessage("info.brand.registered", null,
							LocaleContextHolder.getLocale()));
		}
		return "redirect:/brand/" + brand.getId() + "/edit";

	}

	@RequestMapping(value = "brand/{brandId}/stats", method = RequestMethod.GET)
	public String getStatsAndCosts(@PathVariable("brandId") Long brandId,Model model,
			Principal principal, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		if (!controllerHelperBean.isUserBrandOwner(request, principal, brandId)) {
			controllerHelperBean.throwAccessDeniedException(request);
		}
		Brand brand = brandService.get(brandId);
		Double cost = brandService.getBrandCurrentCost(brandId);
		LocalDate monthBegin = new LocalDate().withDayOfMonth(1);
		LocalDate monthEnd = new LocalDate().plusMonths(1).withDayOfMonth(1).minusDays(1);
		WistiaStats wistiaStats = null;
		try{
		wistiaStats = wistiaApiService.getProjectStats(brand
				.getWistiaProjectData().getHashedId(), monthBegin.toDate(), monthEnd.toDate());
		}catch(HttpServerErrorException e){
			redirectAttributes.addFlashAttribute("info", "Ta strona nie może być wyświetlona z przyczyn niezależnych od nas. Za utrudnienia przepraszamy.");
			return "redirect:/info-page";
		}
		Double totalCost = wistiaStats.getHoursWatched()*3600/1000*cost;
		model.addAttribute("totalCost", totalCost);
		model.addAttribute("startDate", monthBegin.toDate());
		model.addAttribute("endDate", monthEnd.toDate());
		model.addAttribute("hoursWatched", wistiaStats.getHoursWatched());
		model.addAttribute("loadCount", wistiaStats.getLoadCount());
		model.addAttribute("playCount", wistiaStats.getPlayCount());
		return "brand/stats";
	}

}
