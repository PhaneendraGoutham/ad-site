package pl.stalkon.ad.core.model.dao;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao extends pl.styall.library.core.model.defaultimpl.UserDao {
	
	public void addRestrictions(Criteria criteria,String alias, Map<String, Object> params){
		String userAlias = "";
		if(alias != null && !alias.equals("")){
			userAlias = alias+".";
		}
		if(params.containsKey("id")){
			criteria.add(Restrictions.eq(userAlias+"id", new Long((String)params.get("id"))));
		}
	}

}
