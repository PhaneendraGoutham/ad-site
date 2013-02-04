package pl.stalkon.ad.core.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Brand;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;

@Repository
public class BrandDao extends AbstractDao<Brand> {

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;
	
	@SuppressWarnings("unchecked")
	public List<Brand> get(Map<String, Object> queryObject) {
		Criteria adCriteria = currentSession().createCriteria(Brand.class);
		adCriteria.setFetchMode("poster", FetchMode.SELECT);
		if (queryObject.containsKey("brand")) {
			addRestrictions(adCriteria, "brand",
					(Map<String, Object>) queryObject.get("brand"));
		}
		criteriaConfigurer.configureCriteria(adCriteria, queryObject);
//		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Brand> brands = (List<Brand>)adCriteria.list();
		return brands;
	}
	
	public void addRestrictions(Criteria criteria,String alias, Map<String, Object> params){
		String userAlias = "";
		if(alias != null && !alias.equals("")){
			userAlias = alias+".";
		}
		
		if(params.containsKey("id")){
			criteria.add(Restrictions.eq(userAlias+"id", new Long((String)params.get("id"))));
		}
		if(params.containsKey("search")){
			// TODO: hibernate search when performance issues
			criteria.add(Restrictions.like("name", (String)params.get("search"),MatchMode.ANYWHERE));
		}
	}
}
