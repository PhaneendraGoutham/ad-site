package pl.stalkon.ad.rest.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.styall.library.core.rest.ext.SingleObjectResponse;

@Controller
@RequestMapping("/auth")
public class AuthController {

	
	@Autowired
	private ControllerHelperBean controllerHelperBean;
	
	
	@RequestMapping(value = "/brand/{brandId}", method = RequestMethod.GET)
	@ResponseBody
	public SingleObjectResponse isBrandAdmin(@PathVariable("brandId") Long brandId, Principal principal, HttpServletRequest request){
		if(request.isUserInRole("ROLE_COMPANY")){
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			return new SingleObjectResponse(controllerHelperBean.isUserBrandOwner(socialLoggedUser.getUserId(), brandId));
		}
		return new SingleObjectResponse(false);
	}
	
	@RequestMapping(value = "/contest/{contestId}", method = RequestMethod.GET)
	@ResponseBody
	public SingleObjectResponse isContestAdmin(@PathVariable("contestId") Long contestId, Principal principal, HttpServletRequest request){
		if(request.isUserInRole("ROLE_CONTEST")){
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			return new SingleObjectResponse(controllerHelperBean.isContestAdmin(socialLoggedUser.getId(), contestId));
		}
		return new SingleObjectResponse(false);
	}

}
