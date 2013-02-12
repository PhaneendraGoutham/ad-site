package pl.stalkon.ad.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.jpa.JpaUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

import pl.stalkon.ad.core.security.FacebookFetcher;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.core.security.SocialUserServiceImpl;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.ext.SocialUserDataFetcherFactory;
import pl.stalkon.social.facebook.CustomFacebookConnectionFactory;
import pl.stalkon.social.model.SocialUserService;
import pl.stalkon.social.model.AbstractSocialUserServiceImpl;
import pl.styall.library.core.model.defaultimpl.User;

@Configuration
public class SocialConfig {

	@Inject
	private Environment env;

	@Inject
	private TextEncryptor textEncryptor;


	@Bean
	public FacebookFetcher facebookFetcher() {
		return new FacebookFetcher();
	}

	@Bean
	public SocialUserDataFetcherFactory socialUserDataFetcherFactory() {
		Map<String, SocialUserDataFetcher<?>> map = new HashMap<String, SocialUserDataFetcher<?>>();
		map.put("facebook", facebookFetcher());
		return new SocialUserDataFetcherFactory(map);
	}

	@Bean(name = "socialUserService")
	public SocialUserService socialUserService() {
		return new SocialUserServiceImpl();
	}

	@Bean
	public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
		SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();

		// add facebook
		OAuth2ConnectionFactory<Facebook> facebookConnectionFactory = new CustomFacebookConnectionFactory(
				env.getProperty("facebook.appId"),
				env.getProperty("facebook.appSecret"));
		OAuth2AuthenticationService<Facebook> facebookAuthenticationService = new OAuth2AuthenticationService<Facebook>(
				facebookConnectionFactory);
		facebookAuthenticationService.setScope("publish_stream,offline_access,email,user_birthday");
		registry.addAuthenticationService(facebookAuthenticationService);

		return registry;
	}

	/**
	 * When a new provider is added to the app, register its
	 * {@link ConnectionFactory} here.
	 * 
	 * @see FacebookConnectionFactory
	 */
	// @Bean
	// public ConnectionFactoryLocator connectionFactoryLocator() {
	// ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
	// registry.addConnectionFactory(new FacebookConnectionFactory(appId,
	// appSecret));
	// return registry;
	// }

	/**
	 * Singleton data access object providing access to connections across all
	 * users.
	 */
	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		JpaUsersConnectionRepository repository = new JpaUsersConnectionRepository(
				socialUserService(), socialAuthenticationServiceLocator(),
				Encryptors.noOpText());
		repository.setConnectionSignUp(socialUserService());
		return repository;
	}

	/**
	 * Request-scoped data access object providing access to the current user's
	 * connections.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (auth == null) {
			throw new IllegalStateException(
					"Unable to get a ConnectionRepository: no user signed in");
		}
		SocialLoggedUser user = (SocialLoggedUser) auth;
		return usersConnectionRepository().createConnectionRepository(
				user.getUsername());
	}

	/**
	 * A proxy to a request-scoped object representing the current user's
	 * primary Facebook account.
	 * 
	 * @throws NotConnectedException
	 *             if the user is not connected to facebook.
	 */
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook() {
		return connectionRepository().getPrimaryConnection(Facebook.class)
				.getApi();
	}

	// /**
	// * The Spring MVC Controller that allows users to sign-in with their
	// provider accounts.
	// */
	// @Bean
	// public ProviderSignInController providerSignInController() {
	// return new ProviderSignInController(socialAuthenticationServiceLocator(),
	// usersConnectionRepository(),
	// simpleSignInAdapter());
	// }

	@Bean
	public TextEncryptor textEncryptor() {
		return Encryptors.noOpText();
	}

}
