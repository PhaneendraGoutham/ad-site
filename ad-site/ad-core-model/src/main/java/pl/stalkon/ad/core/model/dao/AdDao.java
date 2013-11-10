package pl.stalkon.ad.core.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class AdDao extends AbstractDao<Ad> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private TagDao tagDao;

	@Autowired
	private ContestAdDao contestAdDao;

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;

	@SuppressWarnings("unchecked")
	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList,
			Order order, Paging paging) {
		Criteria adCriteria = prepareCriteria(queryObjectList);
		Long total = (Long) adCriteria.setProjection(Projections.rowCount())
				.uniqueResult();
		adCriteria.setProjection(null).setResultTransformer(
				Criteria.ROOT_ENTITY);
		List<Ad> ads = getAds(adCriteria, order, paging);
		return new AdBrowserWrapper(ads, total);
	}

	@SuppressWarnings("unchecked")
	public List<Ad> getList(List<DaoQueryObject> queryObjectList, Order order,
			Paging paging) {
		Criteria adCriteria = prepareCriteria(queryObjectList);
		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ad> ads = getAds(adCriteria, order, paging);
		return ads;
	}

	private Criteria prepareCriteria(List<DaoQueryObject> queryObjectList) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("user");
		adCriteria.createCriteria("brand");
		addRestrictions(adCriteria, "", queryObjectList);
		return adCriteria;
	}

	private List<Ad> getAds(Criteria adCriteria, Order order, Paging paging) {
		criteriaConfigurer.configureCriteria(adCriteria, order, paging.from, paging.perPage);
		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Ad>) adCriteria.list();
	}

	public boolean addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		boolean added = false;
		for (DaoQueryObject qo : queryObjectList) {
			boolean temp;
			if (qo.name.equals("user")) {
				temp = userDao.addRestrictions(criteria, "user",
						(List<DaoQueryObject>) qo.value);
			} else if (qo.name.equals("contestAd")) {
				criteria.createCriteria("contestAd", "contestAd");
				temp = contestAdDao.addRestrictions(criteria, "contestAd",
						(List<DaoQueryObject>) qo.value);
			} else if (qo.name.equals("brand")) {

				temp = brandDao.addRestrictions(criteria, "brand",
						(List<DaoQueryObject>) qo.value);
			} else if (qo.name.equals("tags")) {
				temp = tagDao.addRestrictions(criteria, "tag",
						(List<DaoQueryObject>) qo.value);
				if (temp)
					criteria.createAlias("tags", "tag");
			} else {
				// qo.addCriteria(criteria, alias, Ad.class);
				temp = qo.addCriteria(criteria, alias);
			}
			if (!added) {
				added = temp;
			}
		}
		return added;
	}

	public boolean isOwner(Long adId, Long userId) {
		Ad ad = get(adId, userId);
		if (ad == null)
			return false;
		return true;
	}

	public Ad get(Long adId, Long userId) {
		Ad ad = (Ad) currentSession()
				.createQuery("from Ad where id = :adId and user = :userId")
				.setLong("adId", adId).setLong("userId", userId).uniqueResult();
		return ad;
	}

	public List<Map<String, Object>> getRatings(List<Long> ids) {
		ScrollableResults results = currentSession()
				.createSQLQuery(
						"SELECT SUM(r.rank)/COUNT(*), COUNT(*), ad_id from ranks as r where r.ad_id in (:ids) Group by r.ad_id")
				.setParameterList("ids", ids).scroll();

		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		while (results.next()) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("rank", results.get()[0]);
			map.put("voteCount", results.get()[1]);
			map.put("id", results.get()[2]);
			resultList.add(map);
		}
		return resultList;
	}

	public Ad getRandom() {
		Criteria criteria = currentSession().createCriteria(Ad.class);
		criteria.createCriteria("user");
		criteria.createCriteria("brand");
		criteria.add(Restrictions.sqlRestriction("approved=1 order by rand()"));
		criteria.setMaxResults(1);
		Ad ad = (Ad) criteria.uniqueResult();
		return ad;
	}

	// public List<Ad> temp(List<Long> brandIds){
	// DetachedCriteria subCriteria = DetachedCriteria.forClass(Brand.class);
	// subCriteria.add(Restrictions.in("id",
	// brandIds)).setProjection(Projections.groupProperty("id"));
	// Criteria adCriteria = currentSession().createCriteria(Ad.class);
	// adCriteria.add(Restrictions.)
	// }
}
