package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ControllerHelperBean controllerHelperBean;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value = "company/register")
	public String getRegistrationPage(Model model){
		Company company = new Company();
		model.addAttribute("company", company);
		model.addAttribute("path", "company/register");
		return "company/register";
	}

	@RequestMapping(value = "/company", method = RequestMethod.POST)
	public String add(@Valid @ModelAttribute("company") Company company,
			BindingResult result, Principal principal, RedirectAttributes redirectAttributes){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		if (result.hasErrors()) {
			controllerHelperBean.invalidPostRequest(redirectAttributes);
		}
		companyService.register(company, socialLoggedUser.getId());
//		controllerHelperBean.reathenticateUser(socialLoggedUser.getUsername());
		redirectAttributes.addFlashAttribute("info", "Przyjeliśmy Twoje zgłoszenie. Poinformujemy Cię o aktywacji firmy na podany przez Ciebie adres email.");
		return "redirect:/info-page";
	}
	
	@RequestMapping(value = "/user/company")
	public String userCompanySite(Model model, Principal principal, HttpServletRequest request){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Company company = companyService.getCompanyWithBrandsByUser(socialLoggedUser.getId());
		if(company.getBrands().size() == 1){
			return "redirect:/brand/"+company.getBrands().get(0).getId().toString();
		}
		model.addAttribute("company", company);
		model.addAttribute("path", "company/base-view");
		return "company/base-view";
	}
	
	
	@RequestMapping(value = "/user/company/{companyId}/approved")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public String changeActive(Model model,@PathVariable("companyId")Long companyId, @RequestParam("approved") Boolean approved){
		Company company = companyService.setApproved(companyId, approved);
		if(approved){
			mailService.sendCompanyVerificationEmail(company);
		}
		return "company/base-view";
	}
	
}
