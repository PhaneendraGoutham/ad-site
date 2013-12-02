package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.security.SocialLoggedUser;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private MailService mailService;
	

	@RequestMapping(value = "/company", method = RequestMethod.POST)
	@ResponseBody
	public Long add(@Valid @RequestBody Company company, Principal principal){
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Company resultCompany = companyService.register(company, socialLoggedUser.getId());
		//mailService.sendCompanyVerificationEmail(resultCompany);
		return resultCompany.getId();
	}
	
	@RequestMapping(value = "/user/{userId}/company")
	@PreAuthorize("principal.id.equals(#userId)")
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
	public void changeActive(@PathVariable("companyId")Long companyId, @RequestParam("approved") Boolean approved){
		Company company = companyService.setApproved(companyId, approved);
		if(approved){
			mailService.sendCompanyVerificationEmail(company);
		}
	}
	
}
