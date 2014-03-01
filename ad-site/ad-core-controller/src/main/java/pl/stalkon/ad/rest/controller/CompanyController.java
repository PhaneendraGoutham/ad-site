package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.MailService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.extensions.AlreadyPostedException;
import pl.styall.library.core.ext.HttpException;
import pl.styall.library.core.rest.ext.SingleObjectResponse;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private MailService mailService;

	@RequestMapping(value = "/company", method = RequestMethod.POST)
	@ResponseBody
	public Long add(@Valid @RequestBody Company company, Principal principal)
			throws HttpException {
		SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
				.getPrincipal();
		Company resultCompany = companyService.register(company,
				socialLoggedUser.getId());
		// mailService.sendCompanyVerificationEmail(resultCompany);
		if (resultCompany != null) {
			mailService.sendCompanyRequest(resultCompany);
			return resultCompany.getId();
		} else {
			throw new AlreadyPostedException();
		}
	}

	@RequestMapping(value = "/company/{companyId}/activate")
	@ResponseBody
	public SingleObjectResponse changeActive(
			@PathVariable("companyId") Long companyId) {
		Company company = companyService.setApproved(companyId, true);
		if (company != null) {
			mailService.sendCompanyRegistrationConfirm(company);
			return new SingleObjectResponse(true);
		}
		return new SingleObjectResponse(false);
	}

}
