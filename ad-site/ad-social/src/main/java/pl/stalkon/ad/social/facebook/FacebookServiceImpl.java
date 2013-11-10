package pl.stalkon.ad.social.facebook;

import java.util.Locale;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.social.OperationNotPermittedException;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookLink;
import pl.stalkon.social.singleconnection.interfaces.AddConnectionHandler;

public class FacebookServiceImpl implements AddConnectionHandler,
		SiteFacebookIntegrator {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private Facebook facebook;

	@Autowired
	private Environment env;

	@Override
	public void afterAddConnection(Connection<?> connection) {
		Facebook facebook = (Facebook) connection.getApi();
		try {
		postOnWall(connection, LocaleContextHolder.getLocale());
		} catch (OperationNotPermittedException e) {
			// ignore
		} catch (NoSuchMessageException e) {
			postOnWall(connection, new Locale("pl")); //TODO: change on default locale
		}
	}

	private void postOnWall(Connection<?> connection, Locale locale) {
		facebook.feedOperations().postLink(
				messageSource.getMessage("social.connectMessage",
						new String[] { connection.getDisplayName() }, locale),
				new FacebookLink(env.getProperty("app.domain"), messageSource
						.getMessage("social.app.name", null, locale),
						messageSource.getMessage("social.appCaption", null,
								locale), messageSource.getMessage(
								"social.appDescription", null, locale)));
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
					new FacebookLink(link, messageSource.getMessage(
							"social.app.name", null,
							LocaleContextHolder.getLocale()), title,
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
