package pl.stalkon.ad.core.model.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAd;
import pl.stalkon.ad.core.model.Inform;
import pl.stalkon.ad.core.model.Rank;
import pl.stalkon.ad.core.model.Tag;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.AdCommentDao;
import pl.stalkon.ad.core.model.dao.AdDao;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.ContestDao;
import pl.stalkon.ad.core.model.dao.InformDao;
import pl.stalkon.ad.core.model.dao.RankDao;
import pl.stalkon.ad.core.model.dao.TagDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.AutocompleteDto;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.styall.library.core.model.dao.DaoQueryObject;
import pl.styall.library.core.model.dao.DaoQueryObject.CompareType;

@Service("adService")
public class AdServiceImpl implements AdService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private AdDao adDao;

	@Autowired
	private InformDao informDao;

	@Autowired
	private RankDao rankDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private AdCommentDao adCommentDao;

	@Autowired
	private ContestDao contestDao;

	@Autowired
	private TagDao tagDao;

	// @Transactional
	// @Override
	// public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList,
	// Order order, Paging ) {
	// return adDao.get(queryObjectList, order, first, last);
	// }

	@Transactional
	@Override
	// @Cacheable(value = "ad", key = "#id")
	public Ad get(Long id, boolean approved) {
		Ad ad = adDao.get(id);
		Hibernate.initialize(ad.getUser());
		Hibernate.initialize(ad.getBrand());
		// ad.getParent().getId();
		return ad;
	}

	@Transactional
	@Override
	@CacheEvict(value = { "waiting" }, allEntries = true)
	public Ad register(AdPostDto adPostDto, Ad ad, Long userId, boolean official) {
		for (Long tagId : adPostDto.getTags()) {
			Tag tag = tagDao.get(tagId);
			ad.addTag(tag);
		}
		User user = userDao.get(userId);
		ad.setUser(user);
		ad.setOfficial(official);
		// ad.setDuration(adPostDto.getDuration());
		ad.setYear(adPostDto.getYear());
		if (adPostDto.getContestId() != null) {
			Contest contest = contestDao.get(adPostDto.getContestId());
			ContestAd contestAd = new ContestAd();
			contestAd.setAd(ad);
			contestAd.setContest(contest);
			ad.setContestAd(contestAd);
			adPostDto.setBrandId(contest.getBrand().getId());
		}
		if (adPostDto.getParentId() != null) {
			Ad parent = adDao.get(adPostDto.getParentId());
			parent.setId(adPostDto.getParentId());
			ad.setParent(parent);
			adPostDto.setBrandId(parent.getBrand().getId());
		}
		Brand brand = brandDao.get(adPostDto.getBrandId());
		// brand.addAdd(ad);
		ad.setBrand(brand);
		ad.setTitle(adPostDto.getTitle());
		ad.setDescription(adPostDto.getDescription());
		ad.setAgeProtected(adPostDto.getAgeProtected());
		ad.setApproved(true);
		adDao.save(ad);
		return ad;
	}

	@Transactional
	@Override
	public void vote(Long adId, Long userId, Short value) {
		Rank rank;
		rank = rankDao.getRankByUserAndAd(userId, adId);
		if (rank == null) {
			User user = userDao.get(userId);
			Ad ad = adDao.get(adId);
			rank = new Rank();
			rank.setUser(user);
			rank.setAd(ad);
		}
		rank.setRank(value);
		rankDao.update(rank);
	}

	@Transactional
	@Override
	public AdComment comment(Long adId, Long userId, Long commentId,
			String message) {
		User user = userDao.get(userId);
		Ad ad = null;
		if (commentId == null) {
			ad = adDao.get(adId);
		}
		AdComment comment = new AdComment();
		comment.setMessage(message);
		comment.setUser(user);
		comment.setAd(ad);
		if (commentId != null) {
			AdComment parent = adCommentDao.get(commentId);
			comment.setParent(parent);
		}
		adCommentDao.save(comment);
		return comment;
	}

	// @Transactional
	// @Override
	// @Cacheable("main")
	// public AdBrowserWrapper getMain(Integer pageFrom, Integer pageCount,
	// boolean approved) {
	// List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
	// queryObjectList.add(new DaoQueryObject("place", Ad.Place.MAIN));
	// if (!approved)
	// queryObjectList.add(new DaoQueryObject("approved", true));
	// return adDao.get(queryObjectList, Order.desc("dateOnMain"),
	// new Integer(pageFrom), new Integer(pageCount));
	// }
	//
	// @Transactional
	// @Override
	// @Cacheable("waiting")
	// public AdBrowserWrapper getWaiting(Integer pageFrom, Integer pageCount,
	// boolean approved) {
	// List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
	// queryObjectList.add(new DaoQueryObject("place", Ad.Place.WAITING));
	// if (!approved)
	// queryObjectList.add(new DaoQueryObject("approved", true));
	// return adDao.get(queryObjectList, Order.desc("creationDate"),
	// new Integer(pageFrom), new Integer(pageCount));
	// }

	@Transactional
	@Override
	@Cacheable(value = "top", key = "#paging.perPage")
	public List<Ad> getTop(Paging paging, Date date) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		if (date != null) {
			queryObjectList.add(new DaoQueryObject("creationDate", date,
					CompareType.MORE));
		}
		queryObjectList.add(new DaoQueryObject("approved", true));
		queryObjectList.add(new DaoQueryObject("place", Place.MAIN));
		return adDao.getList(queryObjectList, Order.desc("rank"), paging);
	}

	@Transactional
	@Override
	public boolean isOwner(Long adId, Long userId) {
		return adDao.isOwner(adId, userId);
	}

	@Transactional
	@Override
	public Ad update(AdPostDto adPostDto, Long adId, Long userId) {
		Ad ad = adDao.get(adId, userId);
		if (ad == null) {
			return null;
		}
		Brand brand = brandDao.get(adPostDto.getBrandId());
		// brand.addAdd(ad);
		// ad.update(adPostDto, getTags(adPostDto.getTagsIds()), brand);
		ad.setBrand(brand);
		adDao.save(ad);
		return ad;
	}

	@Transactional
	@Override
	@CacheEvict(value = { "main", "waiting", "top" }, allEntries = true)
	public void changeApproval(Long id, boolean approved) {
		Ad ad = adDao.get(id);
		if (ad == null)
			return;
		ad.setApproved(approved);
		adDao.update(ad);
	}

	@Transactional
	@Override
	@CacheEvict(value = { "main", "waiting", "top" }, allEntries = true)
	public void changeAgeProtected(Long id, boolean ageProtected) {
		Ad ad = adDao.get(id);
		if (ad == null)
			return;
		ad.setAgeProtected(ageProtected);
		adDao.update(ad);
	}

	@Transactional
	@Override
	@CacheEvict(value = { "main", "waiting", "top" }, allEntries = true)
	public void changePlace(Long id, Place place) {
		Ad ad = adDao.get(id);

		if (ad == null)
			return;
		ad.setPlace(place);
		if (place == Place.MAIN)
			ad.setDateOnMain(new Date());
		adDao.update(ad);
	}

	@Transactional
	private List<Tag> getTags(List<Long> tagsIds) {
		if (tagsIds == null) {
			return null;
		}
		List<Tag> tags = new ArrayList<Tag>();
		for (Long id : tagsIds) {
			Tag t = tagDao.get(id);
			if (t != null) {
				tags.add(t);
			}
		}
		return tags;
	}

	@Override
	@Transactional
	@Cacheable(value = "comments")
	public List<AdComment> getComments(Long adId) {
		Ad ad = adDao.get(adId);
		ad.getComments().size();
		return ad.getComments();
	}

	@Override
	@Transactional
	public Ad rand() {
		return adDao.getRandom();
	}

	@Override
	@Transactional
	@Cacheable("tags")
	public List<Tag> getTags() {
		return tagDao.get();
	}

	@Override
	@Transactional
	@Cacheable(value = "waiting", key = "#paging.from + '-' + #paging.perPage + '-' + #approved")
	public AdBrowserWrapper getWaiting(AdSearchDto adSearchDto, Paging paging,
			boolean approved) {
		adSearchDto.setOrder("desc");
		adSearchDto.setOrderBy("creationDate");
		return get(adSearchDto, paging, approved);
	}

	@Override
	@Transactional
	@Cacheable(value = "main", key = "#paging.from + '-' + #paging.perPage + '-' + #approved")
	public AdBrowserWrapper getMain(AdSearchDto adSearchDto, Paging paging,
			boolean approved) {
		adSearchDto.setOrder("desc");
		adSearchDto.setOrderBy("creationDate");
		return get(adSearchDto, paging, approved);
	}

	@Override
	@Transactional
	public AdBrowserWrapper get(AdSearchDto adSearchDto, Paging paging,
			boolean approved) {
		List<DaoQueryObject> queryObjectList;
		Order order = prepareOrder(adSearchDto);
		if (adSearchDto.getSearch() == null) {
			queryObjectList = glueConditions(adSearchDto,
					approved);
			return adDao.get(queryObjectList, order, paging);
		}else{
			queryObjectList = new ArrayList<DaoQueryObject>(1);
			queryObjectList.add(new DaoQueryObject("search", adSearchDto.getSearch()));
			if (!approved)
				queryObjectList.add(new DaoQueryObject("approved", true));
			return adDao.getByLuceneSearch(queryObjectList, order, paging);
		}
	
		
	}
	
	
	private List<DaoQueryObject> glueConditions(AdSearchDto adSearchDto,
			boolean approved) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		prepareAdConditions(queryObjectList, adSearchDto, approved);
		prepareBrandConditions(queryObjectList, adSearchDto);
		prepareTagConditions(queryObjectList, adSearchDto);
		prepareContestConditions(queryObjectList, adSearchDto);
		prepareUserConditions(queryObjectList, adSearchDto);
		return queryObjectList;
	}

	private Order prepareOrder(AdSearchDto adSearchDto) {
		Order order;
		String orderBy = null;
		if (adSearchDto.getOrderBy().equals("vote")) {
			orderBy = "voteCount";
		} else {
			orderBy = adSearchDto.getOrderBy();
		}
		if (adSearchDto.getOrder().equals("desc")) {
			order = Order.desc(orderBy);
		} else {
			order = Order.asc(orderBy);
		}
		return order;
	}

	private void prepareAdConditions(List<DaoQueryObject> queryObjectList,
			AdSearchDto adSearchDto, boolean approved) {
		queryObjectList.add(new DaoQueryObject("rank", adSearchDto
				.getRankFrom(), CompareType.MORE));
		queryObjectList.add(new DaoQueryObject("rank", adSearchDto.getRankTo(),
				CompareType.LESS));
		queryObjectList.add(new DaoQueryObject("voteCount", adSearchDto
				.getVotesFrom(), CompareType.MORE));
		queryObjectList.add(new DaoQueryObject("voteCount", adSearchDto
				.getVotesTo(), CompareType.LESS));
		queryObjectList.add(new DaoQueryObject("year", adSearchDto
				.getYearFrom(), CompareType.MORE));
		queryObjectList.add(new DaoQueryObject("year", adSearchDto.getYearTo(),
				CompareType.LESS));
		queryObjectList.add(new DaoQueryObject("parent.id", adSearchDto
				.getParentId()));
		if (adSearchDto.getText() != null && !adSearchDto.getText().equals("")) {
			queryObjectList.add(new DaoQueryObject("title", "%"
					+ adSearchDto.getText() + "%", CompareType.LIKE));
		}
		if (adSearchDto.getPlace() != null && adSearchDto.getPlace() > -1) {
			queryObjectList.add(new DaoQueryObject("place",
					Place.values()[adSearchDto.getPlace()]));
		}
		if (!approved)
			queryObjectList.add(new DaoQueryObject("approved", true));
	}

	private void prepareBrandConditions(List<DaoQueryObject> queryObjectList,
			AdSearchDto adSearchDto) {
		if (adSearchDto.getBrandList() != null
				&& adSearchDto.getBrandList().size() > 0) {
			List<DaoQueryObject> brandList = new ArrayList<DaoQueryObject>();
			brandList.add(new DaoQueryObject("id", adSearchDto.getBrandList(),
					CompareType.IN));
			queryObjectList.add(new DaoQueryObject("brand", brandList));
		}
	}

	private void prepareTagConditions(List<DaoQueryObject> queryObjectList,
			AdSearchDto adSearchDto) {
		if (adSearchDto.getTagList() != null
				&& adSearchDto.getTagList().size() > 0) {
			List<DaoQueryObject> tagList = new ArrayList<DaoQueryObject>();
			tagList.add(new DaoQueryObject("id", adSearchDto.getTagList(),
					CompareType.IN_DISJUNCTION));
			queryObjectList.add(new DaoQueryObject("tags", tagList));
			// List<Long> adIds = tagDao.getAdIds(adSearchDto.getTagList());
			// if(adIds != null && adIds.size() > 0){
			// queryObjectList.add(new DaoQueryObject("id", adIds,
			// CompareType.IN));
			// }

		}
	}

	private void prepareContestConditions(List<DaoQueryObject> queryObjectList,
			AdSearchDto adSearchDto) {
		if (adSearchDto.getContestId() != null) {
			List<DaoQueryObject> contestAdQueryObjectList = new ArrayList<DaoQueryObject>();
			contestAdQueryObjectList.add((new DaoQueryObject("contest.id",
					adSearchDto.getContestId())));
			contestAdQueryObjectList.add((new DaoQueryObject("winner",
					adSearchDto.getWinner())));
			queryObjectList.add(new DaoQueryObject("contestAd",
					contestAdQueryObjectList));
		}
	}

	private void prepareUserConditions(List<DaoQueryObject> queryObjectList,
			AdSearchDto adSearchDto) {
		if (adSearchDto.getUserId() != null) {
			List<DaoQueryObject> userQueryObjectList = new ArrayList<DaoQueryObject>();
			userQueryObjectList.add((new DaoQueryObject("id", adSearchDto
					.getUserId())));
			queryObjectList
					.add(new DaoQueryObject("user", userQueryObjectList));
		}
	}

	@Override
	@Transactional
	public List<AutocompleteDto> getTagsByTerm(String term) {
		List<DaoQueryObject> query = new ArrayList<DaoQueryObject>(1);
		query.add(new DaoQueryObject("name", "%" + term + "%", CompareType.LIKE));
		List<Tag> tags = tagDao.get(query, Order.asc("name"), 0, 5);
		List<AutocompleteDto> result = new ArrayList<AutocompleteDto>(
				tags.size());
		for (Tag tag : tags) {
			result.add(new AutocompleteDto(tag.getName(), tag.getId()));
		}
		return result;
	}

	// @Override
	// @Transactional
	// public AdBrowserWrapper getUserAds(Long userId, Integer pageFrom,
	// Integer pageCount, boolean approved) {
	// List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
	// List<DaoQueryObject> userQueryObjectList = new
	// ArrayList<DaoQueryObject>();
	// userQueryObjectList.add((new DaoQueryObject("id", userId)));
	// queryObjectList.add(new DaoQueryObject("user", userQueryObjectList));
	// if (!approved)
	// queryObjectList.add(new DaoQueryObject("approved", true));
	// return adDao.get(queryObjectList, Order.desc("creationDate"),
	// new Integer(pageFrom), new Integer(pageCount));
	// }

	@Override
	@Transactional
	public void informAd(Long id, String message) {
		Ad ad = adDao.get(id);
		Inform inform = new Inform();
		inform.setAd(ad);
		inform.setMessage(message);
		informDao.save(inform);
	}

	@Override
	@Transactional
	public void informComment(Long id, String message) {
		AdComment comment = adCommentDao.get(id);
		Inform inform = new Inform();
		inform.setAdComment(comment);
		inform.setMessage(message);
		informDao.save(inform);
	}

	// @Override
	// @Transactional
	// public AdBrowserWrapper getBrandAds(Long brandId, Integer pageFrom,
	// Integer pageCount, boolean approved) {
	// List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
	// List<DaoQueryObject> brandQueryObjectList = new
	// ArrayList<DaoQueryObject>();
	// brandQueryObjectList.add((new DaoQueryObject("id", brandId)));
	// queryObjectList.add(new DaoQueryObject("brand", brandQueryObjectList));
	// if (!approved)
	// queryObjectList.add(new DaoQueryObject("approved", true));
	// return adDao.get(queryObjectList, Order.desc("creationDate"),
	// new Integer(pageFrom), new Integer(pageCount));
	// }

	@Override
	@Transactional
	@Cacheable(value = "raitings")
	public List<Map<String, Object>> getRatings(List<Long> ids) {
		return adDao.getRatings(ids);
	}

	@Override
	@Transactional
	public List<Ad> getList(AdSearchDto adSearchDto, Paging paging,
			boolean approved) {
		List<DaoQueryObject> queryObjectList = glueConditions(adSearchDto,
				approved);
		Order order = prepareOrder(adSearchDto);
		return adDao.getList(queryObjectList, order, paging);
	}

	@Override
	@Transactional
	public Long getBrandAdsCount(Long brandId) {
		return adDao.getBrandAdsCount(brandId);
	}

	// @Override
	// @Transactional
	// public AdBrowserWrapper getContestAds(Long contestId,
	// ContestAdAndAnswerSearchDto contestAdAndAnswerSearchDto,
	// Integer pageFrom, Integer pageCount, boolean approved) {
	// List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
	// List<DaoQueryObject> contestAdQueryObjectList = new
	// ArrayList<DaoQueryObject>();
	// contestAdQueryObjectList.add((new DaoQueryObject("contest.id",
	// contestId)));
	// queryObjectList.add(new DaoQueryObject("contestAd",
	// contestAdQueryObjectList));
	// if (!approved)
	// queryObjectList.add(new DaoQueryObject("approved", true));
	// return adDao.get(queryObjectList, Order.desc("creationDate"),
	// new Integer(pageFrom), new Integer(pageCount));
	// }

}
