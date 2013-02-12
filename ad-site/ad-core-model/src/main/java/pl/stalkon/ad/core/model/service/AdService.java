package pl.stalkon.ad.core.model.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;

public interface AdService  {
	public Ad register(Ad ad, Long posterId, Long brandId);
	public Ad register(Ad ad, Long posterId);
	public List<Ad> get(Map<String, Object> queryObject);
	public Ad get(Long id);
	public void vote(Long adId, Long userId, Short value);
	public AdComment comment(Long adId, Long userId, AdComment comment);
}
