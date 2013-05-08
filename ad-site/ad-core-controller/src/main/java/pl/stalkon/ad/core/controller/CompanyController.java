package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;
	
	@RequestMapping(value = "company/register")
	public String getRegistrationPage(Model model){
		Company company = new Company();
		model.addAttribute("company", company);
		model.addAttribute("path", "company/register");
		return "company/register";
	}

	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute("company") Company company,
			BindingResult result, Principal principal){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			return "company/register";
		}
		companyService.register(company, socialLoggedUser.getId());
		controllerHelperBean.reathenticateUser(socialLoggedUser.getUsername());
		return "redirect:/user/company";
	}
	
	@RequestMapping(value = "/user/company")
	public String userCompanySite(Model model, Principal principal, HttpServletRequest request){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if(!request.isUserInRole(UserRoleDef.ROLE_COMPANY)){
			return "redirect:/company/register";
		}
		Company company = companyService.getCompanyWithBrandsByUser(socialLoggedUser.getId());
		if(company.getBrands().size() == 1){
			return "redirect:/brand/"+company.getBrands().get(0).getId().toString();
		}
		model.addAttribute("company", company);
		model.addAttribute("path", "company/base-view");
		return "company/base-view";
	}
}
