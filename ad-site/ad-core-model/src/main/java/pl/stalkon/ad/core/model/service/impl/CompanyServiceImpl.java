package pl.stalkon.ad.core.model.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.dao.CompanyDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.service.CompanyService;
import pl.styall.library.core.model.UserRole;

@Service("companyService")
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	@Transactional
	public Company register(Company company, Long userId) {
		User user = userDao.get(userId);
		user.addUserRole(userDao.loadUserRoleByName(UserRoleDef.ROLE_COMPANY));
		company.setUser(user);
		companyDao.save(company);
		return company;
	}

	@Override
	@Transactional
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

	@Override
	@Transactional
	public Company getCompanyWithBrandsByUser(Long userId) {
		Company company = companyDao.getByUser(userId);
		company.getBrands().size();
		return company;
	}

	@Override
	@Transactional
	public boolean isCompanyOfUser(Long userId, Long companyId) {
		Company company = companyDao.get(companyId, userId);
		if(company == null)
			return false;
		return true;
	}

}
