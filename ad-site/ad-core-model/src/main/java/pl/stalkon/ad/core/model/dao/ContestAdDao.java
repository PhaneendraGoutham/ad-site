package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAd;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class ContestAdDao extends AbstractDao<ContestAd> {
	
	
	

//	@Autowired
//	private ContestDao contestDao;
//
//	@Autowired
//	private CriteriaConfigurer criteriaConfigurer;
//
//	@SuppressWarnings("unchecked")
//	public AdBrowserWrapper getAds(List<DaoQueryObject> queryObjectList,
//			Order order, Integer first, Integer last) {
//		Criteria criteria = currentSession().createCriteria(ContestAd.class);
//		Criteria adCriteria = criteria.createCriteria("ad");
//		adCriteria.createCriteria("user");
//		adCriteria.createCriteria("brand");
//		addRestrictions(criteria, "", queryObjectList);
//		Long total = (Long) criteria.setProjection(Projections.rowCount())
//				.uniqueResult();
//
//		criteria.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);
//
//		criteriaConfigurer.configureCriteria(criteria, order, first, last);
//
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		List<Ad> ads = (List<Ad>) criteria.list();
//		return new AdBrowserWrapper(ads, total);
//	}
//
//	public boolean addRestrictions(Criteria criteria, String alias,
//			List<DaoQueryObject> queryObjectList) {
//		boolean added = false;
//		for (DaoQueryObject qo : queryObjectList) {
//			boolean temp;
//			if (qo.name.equals("contest")) {
//				temp = contestDao.addRestrictions(criteria, "contest",
//						(List<DaoQueryObject>) qo.value);
//			} else {
//				// qo.addCriteria(criteria, alias, Ad.class);
//				temp = qo.addCriteria(criteria, alias);
//			}
//			if (!added) {
//				added = temp;
//			}
//		}
//		return added;

	public List<ContestAd> getContestAdsWithAds(Long contestId){
		Criteria criteria = currentSession().createCriteria(ContestAd.class);
		Criteria userCriteria = criteria.createCriteria("ad").createCriteria("user");
		userCriteria.createCriteria("userData");
		userCriteria.createCriteria("socialUser");
		criteria.add(Restrictions.eq("contest.id", contestId));
		return (List<ContestAd>) criteria.list();
	}
	
	public boolean hasUserPostedAd(Long userId, Long contestId){
		Criteria criteria = currentSession().createCriteria(ContestAd.class);
		criteria.add(Restrictions.eq("contest.id", contestId));
		ContestAd contestAd = (ContestAd) criteria.uniqueResult();
		if(contestAd != null && contestAd.getAd().getUser().getId().equals(userId)){
			return true;
		}
		return false;
	}
}
