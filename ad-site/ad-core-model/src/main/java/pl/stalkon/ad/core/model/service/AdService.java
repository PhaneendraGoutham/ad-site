package pl.stalkon.ad.core.model.service;

import java.util.List;
import java.util.Map;

import pl.stalkon.ad.core.model.Ad;

public interface AdService  {
	public Ad register(Ad ad, Long id);
	public List<Ad> get(Map<String, Object> queryObject);
	public Ad get(Long id);
}
