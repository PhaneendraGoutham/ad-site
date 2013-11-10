package pl.stalkon.ad.core.model.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Brand;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class BrandDao extends AbstractDao<Brand> {

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;
	
	public List<Brand> get(){
		Criteria brandCriteria = currentSession().createCriteria(Brand.class);
		brandCriteria.createCriteria("wistiaProjectData");
		brandCriteria.addOrder(Order.asc("name"));
		return (List<Brand>) brandCriteria.list();
//		return (List<Brand>) currentSession().createQuery("from Brand as brand  order by name").list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last ) {
		Criteria brandCriteria = currentSession().createCriteria(Brand.class);
		brandCriteria.setFetchMode("user", FetchMode.SELECT);
		addRestrictions(brandCriteria, "", queryObjectList);
		criteriaConfigurer.configureCriteria(brandCriteria, order, first, last);
//		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Brand> brands = (List<Brand>)brandCriteria.list();
		return brands;
	}
	
	public Double getBrandCurrentCost(Long brandId){
		Criteria criteria = currentSession().createCriteria(Brand.class);
		criteria.createCriteria("costs","cost");
		criteria.add(Restrictions.eq("cost.current", true));
		criteria.setProjection(Projections.property("cost.costPerSecondPer1000"));
		Double cost = (Double) criteria.uniqueResult();
		return cost;
	}
	
//	public void addRestrictions(Criteria criteria, String alias,
//			List<DaoQueryObject> queryObjectList) {
//		for (DaoQueryObject qo : queryObjectList) {
//			qo.addCriteria(criteria, alias, Brand.class);
//		}
//	}
}
