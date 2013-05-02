package pl.stalkon.ad.core.model.service;

import java.util.List;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;


public interface CompanyService {
	public Company register(Company company, Long userId);
	public Company update(Company company, Long companyId,  Long userId);
	public Company getCompanyWithBrandsByUser(Long userId);
	public boolean isCompanyOfUser(Long userId, Long companyId);
}
