package pl.stalkon.ad.core.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.styall.library.core.ext.controller.BaseController;
import pl.styall.library.core.ext.validation.ValidationException;
import pl.styall.library.core.security.authentication.LoggedUser;

@Controller
public class BrandController extends BaseController {

	@Autowired
	private BrandService brandService;
	
	@RequestMapping(value = "brand/", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Long add(@Valid @RequestBody Brand brand, Principal principal) throws ValidationException {
		LoggedUser loggedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
		brandService.register(brand, loggedUser.getId());
		return brand.getId();
	}

}
