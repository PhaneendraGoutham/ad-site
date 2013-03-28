package pl.stalkon.ad.core.model.dao;

import org.springframework.stereotype.Repository;

import pl.stalkon.ad.core.model.Ad;
import pl.styall.library.core.model.dao.AbstractDao;
import pl.stalkon.ad.core.model.Company;

@Repository
public class CompanyDao extends AbstractDao<Company> {
	public Company get(Long companyId, Long userId) {
		Company company = (Company) currentSession()
				.createQuery(
						"from Company where id = :companyId and poster = :userId")
				.setLong("companyId", companyId).setLong("userId", userId)
				.uniqueResult();
		return company;
	}
}
