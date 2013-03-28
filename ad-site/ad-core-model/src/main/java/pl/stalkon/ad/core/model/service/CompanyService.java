package pl.stalkon.ad.core.model.service;

import pl.stalkon.ad.core.model.Company;


public interface CompanyService {
	public Company register(Company company, Long userId);
	public Company update(Company company, Long companyId,  Long userId);
	
}
