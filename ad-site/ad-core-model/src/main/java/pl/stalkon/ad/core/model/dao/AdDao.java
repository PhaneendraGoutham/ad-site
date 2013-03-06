package pl.stalkon.ad.core.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
	public AdBrowserWrapper get(Map<String, Object> queryObject) {
		Criteria adCriteria = currentSession().createCriteria(Ad.class);
		adCriteria.createCriteria("poster");
		if (queryObject.containsKey("user")) {
			userDao.addRestrictions(adCriteria, "poster",
					(Map<String, Object>) queryObject.get("user"));
		}
		if (queryObject.containsKey("brand")) {
			brandDao.addRestrictions(adCriteria, "brand",
					(Map<String, Object>) queryObject.get("brand"));
		}
		addRestrictions(adCriteria, "", queryObject);
		Long total = (Long) adCriteria.setProjection(Projections.rowCount())
				.uniqueResult();

		adCriteria.setProjection(null).setResultTransformer(
				Criteria.ROOT_ENTITY);
		
		criteriaConfigurer.configureCriteria(adCriteria, queryObject);
		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Ad> ads = (List<Ad>) adCriteria.list();
		return new AdBrowserWrapper(ads, total);
	}
	
	public void addRestrictions(Criteria criteria, String alias, Map<String, Object> queryObject){
		alias = alias != ""? alias + ".": "";
		if(queryObject.containsKey("approved")){
			criteria.add(Restrictions.eq(alias + "approved", queryObject.get("approved")));
		}
		if(queryObject.containsKey("place")){
			criteria.add(Restrictions.eq(alias + "place", queryObject.get("place")));
		}
	}
}
