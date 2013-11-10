package pl.stalkon.ad.core.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DailymotionController {

	private Logger log = Logger.getLogger(DailymotionController.class);
	
	
	@RequestMapping(value="/dailymotion/media", method = RequestMethod.POST)
	public void mediaCreated(HttpServletRequest request){
		System.out.println("Marysia se idzie rowna sciezka");
	}
}
