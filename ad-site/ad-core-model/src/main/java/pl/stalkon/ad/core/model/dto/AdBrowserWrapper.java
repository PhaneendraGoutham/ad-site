package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.Ad;

public class AdBrowserWrapper {

	private final List<Ad> ads;
	private final Long total;

	public AdBrowserWrapper(List<Ad> ads, Long total) {
		super();
		this.ads = ads;
		this.total = total;
	}

	public List<Ad> getAds() {
		return ads;
	}

	public Long getTotal() {
		return total;
	}


}
