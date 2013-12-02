package pl.stalkon.ad.rest.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.UserInfoService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.styall.library.core.security.filter.UserMessageSessionAttribute;

@Component
public class ControllerHelperBean {

//	public final int AD_PER_PAGE = 5;
//	public final int CONTESTS_PER_PAGE = 5;
//	public final int CONTESTS_ANSWERS_PER_PAGE = 10;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private MessageSource messageSource;
	
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserInfoService userInfoService;

	
	
	public boolean getActive(Principal principal) {
		if (principal != null) {
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			for (GrantedAuthority ga : socialLoggedUser.getAuthorities()) {
				if (ga.getAuthority().equals(UserRoleDef.ROLE_AD_ADMIN))
					return true;
			}
		}
		return false;
	}

	public void preparePagination(int perPage, Model model, Long count, int page) {
		int pages = (int) Math.ceil(count / ((double) perPage));
		model.addAttribute("pageAmount", pages);
		model.addAttribute("currentPage", page);
	}

//	public int getFrom(int perPage, int page) {
//		page--;
//		return page * perPage;
//	}

	public void reathenticateUser(String username) {
		SocialLoggedUser userDetails = (SocialLoggedUser) userDetailsService
				.loadUserByUsername(username);
		userDetails.eraseCredentials();
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	// public boolean isUserCompanyAdmin(HttpServletRequest request,
	// Principal principal) {
	// if (request.isUserInRole(UserRoleDef.ROLE_COMPANY)) {
	// SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication)
	// principal)
	// .getPrincipal();
	// Company company = companyService
	// .getCompanyWithBrandsByUser(socialLoggedUser.getId());
	// if (company != null) {
	// return true;
	// }
	// }
	// return false;
	// }

	// public boolean isContestAdmin(HttpServletRequest request,
	// Principal principal, Long contestId){
	//
	// if(request.isUserInRole(UserRoleDef.ROLE_COMPANY)){
	// Contes
	// }
	// }

	public boolean isUserBrandOwner(Long userId, Long brandId) {
		Company company = companyService.getCompanyWithBrandsByUser(userId);
		System.out.println(brandId);
		Brand mockBrand = new Brand();
		mockBrand.setId(brandId);
		if (company.getBrands().contains(mockBrand)) {
			return true;
		}
		return false;
	}

	public void throwAccessDeniedException(String message,
			HttpServletRequest request) throws AccessDeniedException {
		if (!request.isUserInRole(UserRoleDef.ROLE_ADMIN)) {
			throw new AccessDeniedException(message);
		}
	}

	public void throwAccessDeniedException(HttpServletRequest request)
			throws AccessDeniedException {
		if (!request.isUserInRole(UserRoleDef.ROLE_ADMIN)) {
			throw new AccessDeniedException(
					messageSource.getMessage("info.access.denied", null,
							LocaleContextHolder.getLocale()));
		}
	}

	public String invalidPostRequest(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("info", messageSource.getMessage(
				"info.incorrect.data", null, LocaleContextHolder.getLocale()));
		return "redirect:/info-page";
	}

	public boolean isContestAdmin(HttpServletRequest request,
			Principal principal, Long contestId) {
		if (request.isUserInRole(UserRoleDef.ROLE_CONTEST)) {
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			if (contestService.isContestAdmin(socialLoggedUser.getId(),
					contestId)) {
				return true;
			}
		}
		return false;
	}
	
	public void setUserMessagesSessionAttr(SocialLoggedUser socialLoggedUser, HttpServletRequest request){
		//TODO: rethink use of this method its a copy of the one from filter
		int userMessagesCount = userInfoService.getUserMessagesCount(socialLoggedUser.getId());
		UserMessageSessionAttribute attr = new UserMessageSessionAttribute();
		attr.setUnhandledMessagesCount(userMessagesCount);
		long currentTimestamp = new Date().getTime();
		attr.setTimestamp(currentTimestamp);
		request.getSession().setAttribute("userMessagesAttribute", attr);
		
	}

}
