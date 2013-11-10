package pl.stalkon.ad.core.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.RequestContextUtils;

import pl.stalkon.ad.core.model.service.AdService;

@Controller
public class CommonController {

	
	
	@Autowired
	private AdService adService;
	
	@RequestMapping(value = "info-page", method = RequestMethod.GET)
	public String show(HttpServletRequest request) {
		
		Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);
		if (map != null)
			return "info-page";
		else
			return "redirect:/";
	}
	
	

}
