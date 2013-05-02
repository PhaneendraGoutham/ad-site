package pl.stalkon.ad.core.model.dao;

import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Rank;
import pl.styall.library.core.model.dao.AbstractDao;

@Repository
public class RankDao extends AbstractDao<Rank> {
	public Rank getRankByUserAndAd(Long userId, Long adId){
		Rank rank = (Rank) currentSession().createQuery("from Rank where user = :userId and ad =:adId").setLong("userId", userId).setLong("adId", adId).uniqueResult();
		return rank;
	}
}
