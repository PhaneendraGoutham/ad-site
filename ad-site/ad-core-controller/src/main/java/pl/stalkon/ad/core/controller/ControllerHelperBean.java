package pl.stalkon.ad.core.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.security.SocialLoggedUser;

@Component
public class ControllerHelperBean {

	public static final int AD_PER_PAGE = 5;
	public static final int CONTESTS_PER_PAGE = 5;
	public static final int CONTESTS_ANSWERS_PER_PAGE = 10;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private ContestService contestService;

	public boolean getActive(Principal principal) {
		if (principal != null) {
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			for (GrantedAuthority ga : socialLoggedUser.getAuthorities()) {
				if (ga.getAuthority().equals(UserRoleDef.ROLE_ADMIN))
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

	public int getFrom(int perPage, int page) {
		page--;
		return page * perPage;
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
	public boolean isUserBrandOwner(HttpServletRequest request,
			Principal principal, Long brandId) {
		if (request.isUserInRole(UserRoleDef.ROLE_COMPANY)) {
			SocialLoggedUser socialLoggedUser = (SocialLoggedUser) ((Authentication) principal)
					.getPrincipal();
			return isUserBrandOwner(socialLoggedUser.getId(), brandId);
		}
		return false;
	}

	public boolean isUserBrandOwner(Long userId, Long brandId) {
		Company company = companyService.getCompanyWithBrandsByUser(userId);
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
			throw new AccessDeniedException("Nie masz uprawnień");
		}
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

}
