package pl.stalkon.ad.core.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dao.CompanyDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.service.CompanyService;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public Company register(Company company, Long userId) {
		User user = userDao.get(userId);
		company.setUser(user);
		companyDao.save(company);
		return company;
	}

	@Override
	public Company update(Company company, Long companyId, Long userId) {
		Company dbCompany = companyDao.get(companyId, userId);
		if(dbCompany == null){
			return null;
		}
		dbCompany.setName(company.getName());
		dbCompany.setNip(company.getNip());
		dbCompany.setPhone(company.getPhone());
		dbCompany.setAddress(company.getAddress());
		companyDao.save(dbCompany);
		return dbCompany;
	}

}
