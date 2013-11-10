package pl.stalkon.ad.core.model.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.UserInfo.Type;
import pl.styall.library.core.model.dao.AbstractDao;

@Repository
public class UserInfoDao extends AbstractDao<UserInfo> {

	public Long getUserInfoCount(Long userId) {
		Long total = (Long) currentSession()
				.createQuery(
						"select count(*) from UserInfo where user = :userId and handled = false")
				.setLong("userId", userId).uniqueResult();
		return total;
	}

	public List<UserInfo> getMessages(Long userId, Boolean handled) {
		// List<UserInfo> userInfos = (List<UserInfo>)
		// currentSession().createQuery("from UserInfo where user = :userId and handled = :handled").setLong("userId",
		// userId).setBoolean("handled", handled).list();
		Criteria criteria = currentSession().createCriteria(UserInfo.class);
		criteria.createCriteria("contest");
		criteria.add(Restrictions.eq("user.id", userId));
		if (handled != null)
			criteria.add(Restrictions.eq("handled", handled));
		criteria.addOrder(Order.asc("handled"));
		criteria.addOrder(Order.desc("creationDate"));
		List<UserInfo> userInfos = (List<UserInfo>) criteria.list();
		return userInfos;
	}

	public boolean setHandled(Long userInfoId, boolean handled, Type type) {
		int count = currentSession()
				.createQuery(
						"update UserInfo set handled= :handled, handled_date = current_timestamp() where id=:userInfoId and type = :type")
				.setBoolean("handled", handled)
				.setLong("userInfoId", userInfoId)
				.setInteger("type", type.ordinal()).executeUpdate();
		return count > 0;
	}

}
