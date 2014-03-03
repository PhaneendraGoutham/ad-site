package pl.stalkon.ad.config;

import java.util.HashMap;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialAuthenticationServiceRegistry;
import org.springframework.social.security.provider.OAuth2AuthenticationService;

import pl.stalkon.ad.core.security.ConnectionSignupImpl;
import pl.stalkon.ad.core.security.FacebookFetcher;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.core.security.SocialUserServiceImpl;
import pl.stalkon.social.ext.SocialServiceHelper;
import pl.stalkon.social.ext.SocialUserDataFetcher;
import pl.stalkon.social.facebook.CustomFacebookConnectionFactory;
import pl.stalkon.social.singleconnection.impl.UsersConnectionRepositoryImpl;
import pl.stalkon.social.singleconnection.interfaces.ConnectionRepository;
import pl.stalkon.social.singleconnection.interfaces.ConnectionSignup;
import pl.stalkon.social.singleconnection.interfaces.SocialUserService;
import pl.stalkon.social.singleconnection.interfaces.UsersConnectionRepository;

@Configuration
public class SocialConfig {

	@Autowired
	private Environment env;

	@Autowired
	private TextEncryptor textEncryptor;

	@Bean
	public FacebookFetcher facebookFetcher() {
		return new FacebookFetcher();
	}

	@Bean
	public SocialServiceHelper socialUserDataFetcherFactory() {
		Map<String, SocialUserDataFetcher<?>> map = new HashMap<String, SocialUserDataFetcher<?>>();
		map.put("facebook", facebookFetcher());
		return new SocialServiceHelper(map);
	}

	@Bean(name = "socialUserService")
	public SocialUserService socialUserService() {
		return new SocialUserServiceImpl();
	}

	@Bean
	public ConnectionSignup connectionSignup() {
		return new ConnectionSignupImpl();
	}

	@Bean
	@Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
	public SocialAuthenticationServiceLocator socialAuthenticationServiceLocator() {
		SocialAuthenticationServiceRegistry registry = new SocialAuthenticationServiceRegistry();

		// add facebook
		OAuth2ConnectionFactory<Facebook> facebookConnectionFactory = new CustomFacebookConnectionFactory(
				env.getProperty("facebook.appId"),
				env.getProperty("facebook.appSecret"));
		OAuth2AuthenticationService<Facebook> facebookAuthenticationService = new OAuth2AuthenticationService<Facebook>(
				facebookConnectionFactory);
		facebookAuthenticationService.setDefaultScope("email,user_birthday");
		registry.addAuthenticationService(facebookAuthenticationService);

		return registry;
	}

//	@Bean
//	@Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
//	public FacebookServiceImpl facebookService() {
//		return new FacebookServiceImpl();
//	}

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
	@Scope(value = "singleton", proxyMode = ScopedProxyMode.INTERFACES)
	public UsersConnectionRepository usersConnectionRepository() {
		UsersConnectionRepositoryImpl repository = new UsersConnectionRepositoryImpl();
//		repository.setConnectionSignUp(connectionSignup());
//		repository.setSocialUserService(socialUserService());
//		repository.setAddConnectionHandler((AddConnectionHandler) facebookService());
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
		SocialLoggedUser user = (SocialLoggedUser) auth.getPrincipal();
		return usersConnectionRepository().createConnectionRepository(
				user.getId());
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

	// @Bean
	// public ConnectController connectController() {
	// ConnectController connectController = new
	// ConnectController(socialAuthenticationServiceLocator(),
	// connectionRepository());
	// connectController.addInterceptor(new PostToWallConnectionInterceptor());
	// return connectController;
	// }

}
