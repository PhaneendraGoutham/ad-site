package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAnswer;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Repository
public class ContestAnswerDao extends AbstractDao<ContestAnswer> {

	@Autowired
	private ContestDao contestDao;

	@Autowired
	private CriteriaConfigurer criteriaConfigurer;

	public boolean hasUserPostedAnswer(Long userId, Long contestId) {
		Long id = (Long) currentSession()
				.createQuery(
						"select answer.id from ContestAnswer as answer where answer.contest = :contestId and answer.user = :userId")
				.setLong("contestId", contestId).setLong("userId", userId)
				.uniqueResult();
		if (id != null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public ContestAnswerBrowserWrapper get(
			List<DaoQueryObject> queryObjectList, Order order, Integer first,
			Integer last) {
		Criteria criteria = currentSession()
				.createCriteria(ContestAnswer.class);
		addRestrictions(criteria, "", queryObjectList);
		Long total = (Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult();

		criteria.setProjection(null).setResultTransformer(Criteria.ROOT_ENTITY);

		criteriaConfigurer.configureCriteria(criteria, order, first, last);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<ContestAnswer> contestAnswers = (List<ContestAnswer>) criteria
				.list();
		return new ContestAnswerBrowserWrapper(contestAnswers, total);
	}

	public boolean addRestrictions(Criteria criteria, String alias,
			List<DaoQueryObject> queryObjectList) {
		boolean added = false;
		for (DaoQueryObject qo : queryObjectList) {
			boolean temp;
			if (qo.name.equals("contest")) {
				temp = contestDao.addRestrictions(criteria, "contest",
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
