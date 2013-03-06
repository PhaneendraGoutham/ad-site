package pl.stalkon.ad.core.model.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Rank;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.AdCommentDao;
import pl.stalkon.ad.core.model.dao.AdDao;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.service.AdService;

@Service("adService")
public class AdServiceImpl implements AdService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private AdCommentDao adCommentDao;
	
	@Transactional
	@Override
	public Ad register(Ad ad, Long id) {
		User poster = userDao.get(id);
		ad.setPoster(poster);
		adDao.add(ad);
		return ad;
	}

	@Transactional
	@Override
	public AdBrowserWrapper get(Map<String, Object> queryObject) {
		return adDao.get(queryObject);
	}
	@Transactional
	@Override
	public Ad get(Long id) {
		Ad ad = adDao.get(id);
		ad.getBrand();
		ad.getComments().size();
		ad.getPoster();
		return ad;
	}

	@Transactional
	@Override
	public Ad register(Ad ad, Long posterId, Long brandId) {
		Brand brand = brandDao.get(brandId);
		brand.addAdd(ad);
		ad.setBrand(brand);
		return register(ad, posterId);
	}
	
	@Transactional
	@Override
	public void vote(Long adId, Long userId, Short value) {
		User user = userDao.get(userId);
		Ad ad = adDao.get(adId);
		Rank rank = new Rank();
		rank.setRank(value);
		rank.setUser(user);
		ad.addRank(rank);
		adDao.update(ad);
	}

	@Transactional
	@Override
	public AdComment comment(Long adId, Long userId, AdComment comment) {
		User user = userDao.get(userId);
		Ad ad = adDao.get(adId);
		comment.setUser(user);
		comment.setAd(ad);
		adCommentDao.save(comment);
		return comment;
	}
	@Transactional
	@Override
	public AdBrowserWrapper getMain(int pageFrom, int pageCount) {
		Map<String, Object> queryObject = new HashMap<String, Object>();
		queryObject.put("poster", new Object());
		queryObject.put("place", Ad.Place.MAIN);
		queryObject.put("pageFrom", pageFrom);
		queryObject.put("pageCount", pageCount);
		queryObject.put("orderBy", "dateOnMain");
		queryObject.put("approved", true);
		return adDao.get(queryObject);
	}
	@Transactional
	@Override
	public AdBrowserWrapper getWaiting(int pageFrom, int pageCount) {
		Map<String, Object> queryObject = new HashMap<String, Object>();
		queryObject.put("pageFrom", pageFrom);
		queryObject.put("pageCount", pageCount);
		queryObject.put("orderBy", "creationDate");
		queryObject.put("approved", true);
		return adDao.get(queryObject);
	}
	
	

}
