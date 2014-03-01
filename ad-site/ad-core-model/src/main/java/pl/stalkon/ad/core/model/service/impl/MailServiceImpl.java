package pl.stalkon.ad.core.model.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Configuration freemarkerConfiguration;

	private String infoSender;
	private String abuseReceiver;
	private String appDomain;
	private String companyReqReceiver;

	private void sendMail(final String subject, final String from,
			final String to, final String templateName, final Object model) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setFrom(from);
				message.setTo(to);
				message.setSubject(subject);
				String text = FreeMarkerTemplateUtils
						.processTemplateIntoString(freemarkerConfiguration
								.getTemplate(templateName, "UTF-8"), model);
				message.setText(text, true);
			}
		};
		mailSender.send(preparator);
	}

	@Override
	public void sendUserVerificationEmail(User user) {
		String subject = "Spotnik.pl - potwierdź założenie konta";
		Map<String, Object> model = new HashMap<String, Object>(1);
		model.put("token", user.getCredentials().getToken());
		model.put("displayName", user.getDisplayName());
		sendMail(subject, infoSender, user.getCredentials().getMail(),
				"userVerificationEmail.ftl", model);
	}

	@Override
	public void sendNewPassword(String mail, String password) {
		String subject = "Spotnik.pl - nowe hasło";
		Map<String, Object> model = new HashMap<String, Object>(1);
		model.put("password", password);
		sendMail(subject, infoSender, mail, "newPasswordEmail.ftl", model);
	}
	
	@Override
	public void sendCompanyRequest(Company company){
		String subject = "Spotnik.pl - nowe hasło";
		Map<String, Object> model = new HashMap<String, Object>(1);
		model.put("company", company);
		sendMail(subject, infoSender, companyReqReceiver, "companyRequestEmail.ftl", model);
	}
	
	@Override
	public void sendCompanyRegistrationConfirm(Company company) {
		String subject = "Spotnik.pl - potwierdzenie partnerstwa";
		sendMail(subject, infoSender, company.getUser().getCredentials().getMail(), "companyRegistrationConfirmEmail.ftl", null);
	}

	@Override
	public void sendCompanyVerificationEmail(Company company) {
		MimeMessage mm = mailSender.createMimeMessage();
		try {
			mm.setSubject(messageSource.getMessage(
					"company.verification.mail.subject", null,
					LocaleContextHolder.getLocale()));

			mm.setRecipient(RecipientType.TO, new InternetAddress(company
					.getUser().getCredentials().getMail()));
			mm.setFrom(new InternetAddress(infoSender));
			mm.setText(messageSource.getMessage(
					"company.verification.mail.text", null,
					LocaleContextHolder.getLocale()), "utf-8", "html");
		} catch (NoSuchMessageException e) {
			// TODO: co z tym zrobic????
		} catch (MessagingException e) {

		}
		mailSender.send(mm);
	}

	@Override
	public void sendAdAbuseMessage(Long id, String message) {
		String subject = "Spotnik.pl - nadużycie reklamy";
		Map<String, Object> model = new HashMap<String, Object>(1);
		model.put("message", message);
		model.put("id", id);
		sendMail(subject, infoSender, abuseReceiver, "adAbuseEmail.ftl", model);
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

	public void setAbuseReceiver(String abuseReceiver) {
		this.abuseReceiver = abuseReceiver;
	}

	public void setCompanyReqReceiver(String companyReqReceiver) {
		this.companyReqReceiver = companyReqReceiver;
	}


}
