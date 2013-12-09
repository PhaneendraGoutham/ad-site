package pl.stalkon.ad.core.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.stalkon.ad.core.model.Ad;
import pl.styall.library.core.rest.ext.EntityDtmMapper;


public class AdsMapWrapper {

	private final List<Map<String, Object>> ads;
	private final Long total;
	
	public AdsMapWrapper(AdBrowserWrapper adBrowserWrapper, EntityDtmMapper entityDtmmapper) {
		this.total = adBrowserWrapper.getTotal();
		ads = new ArrayList<Map<String, Object>>(adBrowserWrapper.getResultList().size());
		
		for(Ad ad: adBrowserWrapper.getResultList()){
			ads.add(entityDtmmapper.mapEntityToDtm(ad, Ad.class, Ad.JSON_SHOW));
		}
	}

	public List<Map<String, Object>> getAds() {
		return ads;
	}

	public Long getTotal() {
		return total;
	}

}
