package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Tag;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class TagDao extends AbstractDao<Tag> {
	
	@Autowired
	private CriteriaConfigurer criteriaConfigurer;
	
	public List<Tag> get(){
		return (List<Tag>)currentSession().createQuery("from Tag order by name").list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Tag> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last ) {
		Criteria tagCriteria = currentSession().createCriteria(Tag.class);
		tagCriteria.setFetchMode("user", FetchMode.SELECT);
		addRestrictions(tagCriteria, "", queryObjectList);
		criteriaConfigurer.configureCriteria(tagCriteria, order, first, last);
//		adCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Tag> tags = (List<Tag>)tagCriteria.list();
		return tags;
	}
//	public boolean addRestrictions(Criteria criteria, String alias,
//			List<DaoQueryObject> queryObjectList) {
//		boolean added = false;
//		for (DaoQueryObject qo : queryObjectList) {
//			boolean temp = qo.addCriteria(criteria, alias, Tag.class);
//			if(!added){
//				added = temp;
//			}
//		}
//		return added;
//	}
}
