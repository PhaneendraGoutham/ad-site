package pl.stalkon.ad.core.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.validator.internal.util.ReflectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;

@Repository
public class AdDao extends AbstractDao<Ad> {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;

	@SuppressWarnings("unchecked")
	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList,
			Order order, Integer first, Integer last) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("poster");
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
	public List<Ad> getList(List<DaoQueryObject> queryObjectList,
			Order order, Integer first, Integer last) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("poster");
		addRestrictions(adCriteria, "", queryObjectList);
		criteriaConfigurer.configureCriteria(adCriteria, order, first, last);
		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ad> ads = (List<Ad>) adCriteria.list();
		return ads;
	}

	public void addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		for (DaoQueryObject qo : queryObjectList) {
			if (qo.name.equals("user")) {
				userDao.addRestrictions(criteria, "poster",
						(List<DaoQueryObject>) qo.value);
			}
			if (qo.name.equals("brand")) {
				brandDao.addRestrictions(criteria, "brand",
						(List<DaoQueryObject>) qo.value);
			}
			qo.addCriteria(criteria, alias, Ad.class);
		}
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
}
