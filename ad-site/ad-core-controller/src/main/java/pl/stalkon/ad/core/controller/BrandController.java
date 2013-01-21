package pl.stalkon.ad.core.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
import pl.styall.library.core.ext.QueryObject;
import pl.styall.library.core.ext.QueryObjectWrapper;
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
	public String add(@Valid @RequestBody Brand brand, Principal principal) throws ValidationException {
//		LoggedUser loggedUser = (LoggedUser) ((Authentication) principal).getPrincipal();
		brandService.register(brand, null);
		return brand.getId();
	}
	
	@RequestMapping(value = "**/brand/", method = RequestMethod.GET, headers = "Accept=application/json")
	@ResponseBody
	public List<Brand> get(@QueryObject QueryObjectWrapper queryObjectWrapper) throws ValidationException {
		List<Brand> brands = brandService.get(queryObjectWrapper.queryObject);
		return brands;
	}

}
