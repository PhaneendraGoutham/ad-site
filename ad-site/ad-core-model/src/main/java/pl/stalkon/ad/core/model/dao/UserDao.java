package pl.stalkon.ad.core.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.User;



@Repository
public class UserDao extends pl.styall.library.core.model.dao.UserDao<User>{
	
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
