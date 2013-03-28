package pl.stalkon.ad.core.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;

@Repository
public class BrandDao extends AbstractDao<Brand> {

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;
	
	@SuppressWarnings("unchecked")
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last ) {
		Criteria brandCriteria = currentSession().createCriteria(Brand.class);
		brandCriteria.setFetchMode("poster", FetchMode.SELECT);
		addRestrictions(brandCriteria, "", queryObjectList);
		criteriaConfigurer.configureCriteria(brandCriteria, order, first, last);
//		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Brand> brands = (List<Brand>)brandCriteria.list();
		return brands;
	}
	
	public void addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		for (DaoQueryObject qo : queryObjectList) {
			qo.addCriteria(criteria, alias, Brand.class);
		}
	}
}
