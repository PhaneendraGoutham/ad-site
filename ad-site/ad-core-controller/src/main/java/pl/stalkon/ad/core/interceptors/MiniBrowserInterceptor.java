package pl.stalkon.ad.core.interceptors;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;

public class MiniBrowserInterceptor implements HandlerInterceptor {

	private AdService adService;

	public void setAdService(AdService adService) {
		this.adService = adService;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView model)
			throws Exception {
		if (model != null) {
			List<Ad> ads = adService.getTop(new Paging(10), null);
			model.addObject("leftTopAds", ads);
		}
	}

	@Override
	public boolean preHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
