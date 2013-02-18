package pl.stalkon.ad.social.facebook;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.UrlPathHelper;

import pl.stalkon.ad.core.AppConstants;
import pl.stalkon.social.authentication.PostToWallService;

public class FacebookServiceImpl implements PostToWallService,
		SiteFacebookIntegrator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Facebook facebook;

	@Autowired
	private Environment env;

	@Override
	public void postAfterAddedConnection(Connection<?> connection) {
		Facebook facebook = (Facebook) connection.getApi();
		try {
			facebook.feedOperations().postLink(
					messageSource.getMessage("social.connectMessage", null,
							LocaleContextHolder.getLocale()),
					new FacebookLink(AppConstants.HTTP
							+ AppConstants.APP_DOMAIN, messageSource
							.getMessage("social.app.name", null,
									LocaleContextHolder.getLocale()),
							messageSource.getMessage("social.appCaption", null,
									LocaleContextHolder.getLocale()),
							messageSource.getMessage("social.appDescription",
									null, LocaleContextHolder.getLocale())));
		} catch (OperationNotPermittedException e) {
			// ignore
		}
	}

	@Override
	public void notifyOnComment(String link, String title, String description) {
	}

	@Override
	public void notifyOnAdCreated(String link, String title, String description) {
		try {
			facebook.feedOperations().postLink(
					messageSource.getMessage("social.ad.create.message", null,
							LocaleContextHolder.getLocale()),
					new FacebookLink(link, messageSource.getMessage("app.name",
							null, LocaleContextHolder.getLocale()), title,
							description));
		} catch (OperationNotPermittedException e) {
			// ignore
		}
	}

	@Override
	public void notifyOnVote(String link, String title, String description,
			short rank) {
	}

}
