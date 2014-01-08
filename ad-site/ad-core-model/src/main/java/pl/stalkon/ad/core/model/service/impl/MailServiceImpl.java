package pl.stalkon.ad.core.model.service.impl;


import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	private String infoSender;

	private String appDomain;

	@Override
	public void sendUserVerificationEmail(User user) {
		MimeMessage mm = mailSender.createMimeMessage();
		try {
			mm.setSubject(messageSource.getMessage(
					"user.verification.mail.subject", null,
					LocaleContextHolder.getLocale()));

			mm.setRecipient(RecipientType.TO, new InternetAddress(user
					.getCredentials().getMail()));
			mm.setFrom(new InternetAddress(infoSender));
			mm.setText(messageSource.getMessage("user.verification.mail.text",
					new Object[] {
							user.getDisplayName(),
							appDomain + "api/user/activate/"
									+ user.getCredentials().getToken() },
					LocaleContextHolder.getLocale()), "utf-8", "html");
		} catch (NoSuchMessageException e) {
			// TODO: co z tym zrobic????
		} catch (MessagingException e) {

		}
		mailSender.send(mm);
	}

	@Override
	public void sendNewPassword(String mail, String password) {
		MimeMessage mm = mailSender.createMimeMessage();
		try {
			mm.setSubject(messageSource.getMessage(
					"user.password.mail.subject", null,
					LocaleContextHolder.getLocale()));

			mm.setRecipient(RecipientType.TO, new InternetAddress(mail));
			mm.setFrom(new InternetAddress(infoSender));
			mm.setText(
					messageSource.getMessage("user.password.mail.text",
							new Object[] { password },
							LocaleContextHolder.getLocale()), "utf-8", "html");
		} catch (NoSuchMessageException e) {
			// TODO: co z tym zrobic????
		} catch (MessagingException e) {

		}
		mailSender.send(mm);

	}
	
	@Override
	public void sendCompanyVerificationEmail(Company company) {
		MimeMessage mm = mailSender.createMimeMessage();
		try {
			mm.setSubject(messageSource.getMessage(
					"company.verification.mail.subject", null,
					LocaleContextHolder.getLocale()));

			mm.setRecipient(RecipientType.TO, new InternetAddress(company.getUser().getCredentials().getMail()));
			mm.setFrom(new InternetAddress(infoSender));
			mm.setText(
					messageSource.getMessage("company.verification.mail.text",
							null,
							LocaleContextHolder.getLocale()), "utf-8", "html");
		} catch (NoSuchMessageException e) {
			// TODO: co z tym zrobic????
		} catch (MessagingException e) {

		}
		mailSender.send(mm);
	}

	public String getInfoSender() {
		return infoSender;
	}

	public void setInfoSender(String infoSender) {
		this.infoSender = infoSender;
	}

	public String getAppDomain() {
		return appDomain;
	}

	public void setAppDomain(String appDomain) {
		this.appDomain = appDomain;
	}


}
