package pl.stalkon.ad.core.model.service;

import pl.stalkon.ad.core.model.User;

public interface MailService {
	
	public void sendUserVerificationEmail(User user);

}
