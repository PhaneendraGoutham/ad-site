package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.query.hibernate.impl.ProjectionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class ContestDao extends AbstractDao<Contest> {

//	@SuppressWarnings("unchecked")
//	public ContestBrowserWrapper get(int first, int last) {
//		Criteria contestCriteria = currentSession().createCriteria(
//				Contest.class);
//		contestCriteria.add(Restrictions.eq("active", true));
//		contestCriteria.addOrder(Order.desc("state"));
//		contestCriteria.addOrder(Order.desc("creationDate"));
//		Long total = (Long) contestCriteria.setProjection(
//				Projections.rowCount()).uniqueResult();
//
//		contestCriteria.setProjection(null).setResultTransformer(
//				Criteria.ROOT_ENTITY);
//		contestCriteria.setFirstResult(first);
//		contestCriteria.setMaxResults(last);
//		List<Contest> contests = (List<Contest>) contestCriteria.list();
//		return new ContestBrowserWrapper(contests, total);
//	}
	@Autowired
	private BrandDao brandDao;
	
	@SuppressWarnings("unchecked")
	public ContestBrowserWrapper get(List<DaoQueryObject> queryObjectList,
			Order order, Integer first, Integer last) {
		Criteria contestCriteria = currentSession().createCriteria(
				Contest.class);
		addRestrictions(contestCriteria, "", queryObjectList);
		Long total = (Long) contestCriteria.setProjection(Projections.rowCount())
				.uniqueResult();

		contestCriteria.setProjection(null).setResultTransformer(
				Criteria.ROOT_ENTITY);
		
		contestCriteria.addOrder(Order.desc("state"));
		contestCriteria.addOrder(Order.desc("creationDate"));
		contestCriteria.setFirstResult(first);
		contestCriteria.setMaxResults(last);

		contestCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Contest> contests = (List<Contest>) contestCriteria.list();
		return new ContestBrowserWrapper(contests, total);
	}
	
	public boolean addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		boolean added = false;
		for (DaoQueryObject qo : queryObjectList) {
			boolean temp;
			if (qo.name.equals("brand")) {
				temp = brandDao.addRestrictions(criteria, "brand",
						(List<DaoQueryObject>) qo.value);
			} else {
				// qo.addCriteria(criteria, alias, Ad.class);
				temp = qo.addCriteria(criteria, alias);
			}
			if (!added) {
				added = temp;
			}
		}
		return added;
	}

}
