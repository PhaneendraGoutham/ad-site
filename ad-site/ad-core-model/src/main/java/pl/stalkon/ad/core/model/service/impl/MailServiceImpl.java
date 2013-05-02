package pl.stalkon.ad.core.model.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Environment env;


	@Override
	public void sendUserVerificationEmail(User user) {
		MimeMessage mm = mailSender.createMimeMessage();
		try {
			mm.setSubject(messageSource.getMessage(
					"user.verification.mail.subject", null,
					LocaleContextHolder.getLocale()));

			mm.setRecipient(RecipientType.TO, new InternetAddress(user
					.getCredentials().getMail()));
			mm.setFrom(new InternetAddress(env.getProperty("mail.inform.from")));
			mm.setText(messageSource.getMessage("user.verification.mail.text",
					new Object[] { user.getDisplayName(),
							env.getProperty("app.domain") +"user/activate/" +user.getCredentials().getToken() },
					LocaleContextHolder.getLocale()), "utf-8", "html");
		} catch (NoSuchMessageException e) {
			//TODO: co z tym zrobic????
		} catch (MessagingException e) {

		}
		mailSender.send(mm);
	}
}