package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.Ad;

public class AdBrowserWrapper extends BrowserWrapper<Ad> {

	public AdBrowserWrapper(List<Ad> resultList, Long total) {
		super(resultList, total);
	}



}
