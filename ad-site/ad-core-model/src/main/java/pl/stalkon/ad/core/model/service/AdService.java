package pl.stalkon.ad.core.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.Order;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.DailymotionData;
import pl.stalkon.ad.core.model.dao.DaoQueryObject;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;

public interface AdService  {
	public Ad register(AdPostDto adPostDto, DailymotionData dailymotionData, Long posterId);
	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last );
	public Ad get(Long id);
	public void vote(Long adId, Long userId, Short value);
	public AdComment comment(Long adId, Long userId, AdComment comment);
	public AdBrowserWrapper getMain(int pageFrom, int pageCount);
	public AdBrowserWrapper getWaiting(int pageFrom, int pageCount);
	public List<Ad> getTop(int pageFrom, int pageCount, Date date);
	public boolean isOwner(Long adId, Long userId);
	public Ad update(AdPostDto adPostDto,Long adId, Long userId);
	public void changeApproval(Long id, boolean approved);
	public void changeAgeProtected(Long id, boolean ageProtected);
	public void putOnMain(Long id);
}
