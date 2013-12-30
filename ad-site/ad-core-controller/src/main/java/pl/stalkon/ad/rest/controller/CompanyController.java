package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
	
	
	
//	@RequestMapping(value = "/user/company/{companyId}/approved")
//	@ResponseBody
//	public void changeActive(@PathVariable("companyId")Long companyId, @RequestParam("approved") Boolean approved){
//		Company company = companyService.setApproved(companyId, approved);
//		if(approved){
//			mailService.sendCompanyVerificationEmail(company);
//		}
//	}
	
}
