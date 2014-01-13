package pl.stalkon.ad.core.model.service;

import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.User;

public interface MailService {
	
	public void sendUserVerificationEmail(User user);
	public void sendNewPassword(String mail, String password);
	public void sendCompanyVerificationEmail(Company company);
	void sendAdAbuseMessage(Long id, String message);
}
