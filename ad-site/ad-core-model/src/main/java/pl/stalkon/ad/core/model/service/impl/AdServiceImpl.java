package pl.stalkon.ad.core.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.DailymotionData;
import pl.stalkon.ad.core.model.Rank;
import pl.stalkon.ad.core.model.Tag;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.dao.AdCommentDao;
import pl.stalkon.ad.core.model.dao.AdDao;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.DaoQueryObject;
import pl.stalkon.ad.core.model.dao.TagDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
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
	
	@Autowired
	private TagDao tagDao;
	

	@Transactional
	@Override
	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last ) {
		return adDao.get(queryObjectList, order, first, last);
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
	public Ad register(AdPostDto adPostDto, DailymotionData dailymotionData, Long posterId) {
		Ad ad = new Ad();
		User poster = userDao.get(posterId);
		ad.setPoster(poster);
		Brand brand = brandDao.get(adPostDto.getBrandId());
		brand.addAdd(ad);
		ad.setDailymotionData(dailymotionData);
		ad.update(adPostDto, getTags(adPostDto.getTagsIds()), brand);
		adDao.save(ad);
		return ad;
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
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		queryObjectList.add(new DaoQueryObject("place", Ad.Place.MAIN));
		queryObjectList.add(new DaoQueryObject("pageFrom", pageFrom));
		queryObjectList.add(new DaoQueryObject("pageCount", pageCount));
		queryObjectList.add(new DaoQueryObject("approved", true));
		return adDao.get(queryObjectList, Order.desc("dateOnMain"), new Integer(pageFrom), new Integer(pageCount));
	}
	@Transactional
	@Override
	public AdBrowserWrapper getWaiting(int pageFrom, int pageCount) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		queryObjectList.add(new DaoQueryObject("approved", true));
		return adDao.get(queryObjectList, Order.desc("creationDate"), new Integer(pageFrom), new Integer(pageCount));
	}
	@Transactional
	@Override
	public List<Ad> getTop(int pageFrom, int pageCount, Date date) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		if(date!=null){
			queryObjectList.add(new DaoQueryObject("creationDate", date));
		}
		queryObjectList.add(new DaoQueryObject("approved", true));
		return adDao.getList(queryObjectList, Order.desc("rank"), new Integer(pageFrom), new Integer(pageCount));
	}

	@Transactional
	@Override
	public boolean isOwner(Long adId, Long userId) {
		return adDao.isOwner(adId, userId);
	}

	@Transactional
	@Override
	public Ad update(AdPostDto adPostDto, Long adId, Long userId) {
		Ad ad =  adDao.get(adId, userId);
		if(ad == null){
			return null;
		}
		Brand brand = brandDao.get(adPostDto.getBrandId());
		brand.addAdd(ad);
		ad.update(adPostDto, getTags(adPostDto.getTagsIds()), brand);
		adDao.save(ad);
		return ad;
	}
	
	@Transactional
	@Override
	public void changeApproval(Long id, boolean approved){
		Ad ad = adDao.get(id);
		if(ad == null)
			return;
		ad.setApproved(approved);
	}
	
	@Transactional
	@Override
	public void changeAgeProtected(Long id, boolean ageProtected){
		Ad ad = adDao.get(id);
		if(ad == null)
			return;
		ad.setAgeProtected(ageProtected);
	}
	
	@Transactional
	@Override
	public void putOnMain(Long id){
		Ad ad = adDao.get(id);
		if(ad == null)
			return;
		ad.setPlace(Place.MAIN);
		ad.setDateOnMain(new Date());
	}
	
	@Transactional
	private List<Tag> getTags(List<Long> tagsIds){
		if(tagsIds == null){
			return null;
		}
		List<Tag> tags = new ArrayList<Tag>();
		for(Long id : tagsIds){
			Tag t = tagDao.get(id);
			if(t != null){
				tags.add(t);
			}
		}
		return tags;
	}

	
}
