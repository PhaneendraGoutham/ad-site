package pl.stalkon.ad.core.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.dto.BrandPostDto;
import pl.stalkon.ad.core.model.dto.BrandSearchDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.video.api.service.impl.WistiaApiService;

@Controller
public class BrandController {

	@Autowired
	private BrandService brandService;

	@Autowired
	private WistiaApiService wistiaApiService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private FileService fileService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AdService adService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;

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
			Principal principal, HttpServletRequest request) {
		Brand brand = brandService.get(brandId);
		model.addAttribute("brand", brand);
		AdBrowserWrapper adBrowserWrapper = adService.getBrandAds(brandId,
				controllerHelperBean.getFrom(ControllerHelperBean.AD_PER_PAGE,
						page), ControllerHelperBean.AD_PER_PAGE,
				controllerHelperBean.getActive(principal));
		controllerHelperBean.preparePagination(
				ControllerHelperBean.AD_PER_PAGE, model,
				adBrowserWrapper.getTotal(), page);
		model.addAttribute("adBrowserWrapper", adBrowserWrapper);
		model.addAttribute(
				"brandAdmin",
				controllerHelperBean.isUserBrandOwner(request, principal,
						brandId) || request.isUserInRole(UserRoleDef.ROLE_ADMIN));
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
	public String getEditPage(Model model, @PathVariable("brandId") Long brandId) {
		Brand brand = brandService.get(brandId);
		BrandPostDto brandPostDto = new BrandPostDto();
		brandPostDto.setDescription(brand.getDescription());
		brandPostDto.setName(brand.getName());
		model.addAttribute("brandPostDto", brandPostDto);
		model.addAttribute("path", "brand/register");
		model.addAttribute("actionUrl", "brand/" + brandId);
		return "brand/register";
	}

	@RequestMapping(value = "brand/{brandId}", method = RequestMethod.POST)
	public String update(Model model, @PathVariable("brandId") Long brandId,
			Principal principal,
			@Valid @ModelAttribute("brandPostDto") BrandPostDto brandPostDto,
			BindingResult result, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Company company = companyService
				.getCompanyWithBrandsByUser(socialLoggedUser.getId());
		Brand mockBrand = new Brand();
		mockBrand.setId(brandId);
		if (!company.getBrands().contains(mockBrand))
			controllerHelperBean.throwAccessDeniedException(request);
		return registerOrUpdateBrand(brandPostDto, result, null, brandId,
				request,redirectAttributes);
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
			BindingResult result, HttpServletRequest request,RedirectAttributes redirectAttributes) {
		return registerOrUpdateBrand(brandPostDto, result, null, null, request, redirectAttributes);
	}

	@RequestMapping(value = "/company/{companyId}/brand", method = RequestMethod.POST)
	public String addByCompany(
			@Valid @ModelAttribute("brandPostDto") BrandPostDto brandPostDto,
			BindingResult result, Principal principal,RedirectAttributes redirectAttributes,
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
		fileService.validateFile(result, brandPostDto.getLogo(), "logo");
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

		if (!brandPostDto.getLogo().isEmpty()) {
			String baseUrl = String.format("%s://%s:%d%s/",
					request.getScheme(), request.getServerName(),
					request.getServerPort(), request.getContextPath());
			try {
				String logoName = fileService.getPath("brands", "logo", null,
						brand.getId().toString());
				String ext = fileService.saveFile(logoName,
						brandPostDto.getLogo(), 250, 90);
				logoName = fileService.getPath("brands", "logo", "small", brand
						.getId().toString());
				fileService.saveFile(logoName, brandPostDto.getLogo(), 120, 40);
				brandService.setBrandLogo(baseUrl + "resources/brands/logo-"
						+ brand.getId() + "." + ext, baseUrl
						+ "resources/brands/logo-" + brand.getId() + "-small."
						+ ext, brand.getId());
			} catch (IOException e) {
				// TODO
			}
		}
		if (brandId == null) {
			return "redirect:/user/company";
		} else {
			return "redirect:/brand/" + brandId;
		}
	}
	// private void registerBrand(BrandPostDto brandPostDto){
	//
	// }

}
