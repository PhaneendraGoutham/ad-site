package pl.stalkon.ad.core.model.dao;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Brand;
import pl.styall.library.core.model.dao.AbstractDao;

@Repository
public class BrandDao extends AbstractDao<Brand> {

	public void addRestrictions(Criteria criteria,String alias, Map<String, Object> params){
		String userAlias = "";
		if(alias != null && !alias.equals("")){
			userAlias = alias+".";
		}
		
		if(params.containsKey("id")){
			criteria.add(Restrictions.eq(userAlias+"id", new Long((Integer)params.get("id"))));
		}
		
	}
}
