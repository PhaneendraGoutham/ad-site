package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
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
	private CriteriaConfigurer criteriaConfigurer;

	@SuppressWarnings("unchecked")
	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList,
			Order order, Integer first, Integer last) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("poster");
		adCriteria.createCriteria("brand");
		addRestrictions(adCriteria, "", queryObjectList);
		Long total = (Long) adCriteria.setProjection(Projections.rowCount())
				.uniqueResult();

		adCriteria.setProjection(null).setResultTransformer(
				Criteria.ROOT_ENTITY);

		criteriaConfigurer.configureCriteria(adCriteria, order, first, last);

		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ad> ads = (List<Ad>) adCriteria.list();
		return new AdBrowserWrapper(ads, total);
	}

	@SuppressWarnings("unchecked")
	public List<Ad> getList(List<DaoQueryObject> queryObjectList, Order order,
			Integer first, Integer last) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("poster");
		addRestrictions(adCriteria, "", queryObjectList);
		criteriaConfigurer.configureCriteria(adCriteria, order, first, last);
		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ad> ads = (List<Ad>) adCriteria.list();
		return ads;
	}

	public boolean addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		boolean added = false;
		for (DaoQueryObject qo : queryObjectList) {
			boolean temp;
			if (qo.name.equals("user")) {
				temp = userDao.addRestrictions(criteria, "poster",
						(List<DaoQueryObject>) qo.value);
			} else if (qo.name.equals("brand")) {
				temp =brandDao.addRestrictions(criteria, "brand",
						(List<DaoQueryObject>) qo.value);
			} else if (qo.name.equals("tags")) {
				temp = tagDao.addRestrictions(criteria, "tag",
						(List<DaoQueryObject>) qo.value);
				if(temp)
					criteria.createAlias("tags", "tag");
			} else {
//				qo.addCriteria(criteria, alias, Ad.class);
				temp = qo.addCriteria(criteria, alias);
			}
			if(!added){
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
				.createQuery("from Ad where id = :adId and poster = :userId")
				.setLong("adId", adId).setLong("userId", userId).uniqueResult();
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
