package pl.stalkon.ad.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SocialAuthenticationProvider;
import org.springframework.social.security.SocialAuthenticationServiceLocator;
import org.springframework.social.security.SocialUserDetailsService;

import pl.stalkon.social.authentication.AuthenticationUserIdExtractor;
import pl.stalkon.social.authentication.SocialUserDetailsServiceImpl;

@Configuration
public class SocialSecurityConfig {

	@Inject
	private Environment environment;

	@Inject
	private UsersConnectionRepository usersConnectionRepository;

	@Bean 
	public SocialAuthenticationFilter socialAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices, SocialAuthenticationServiceLocator authenticationServiceLocator) {
		SocialAuthenticationFilter socialAuthenticationFilter = new SocialAuthenticationFilter(authenticationManager, userIdSource(), usersConnectionRepository, authenticationServiceLocator);
		socialAuthenticationFilter.setSignupUrl("/ad-web/signup"); // TODO: Fix filter to handle in-app paths
		socialAuthenticationFilter.setRememberMeServices(rememberMeServices);
		socialAuthenticationFilter.setFilterProcessesUrl("/signin");
		return socialAuthenticationFilter;
	}

	@Bean
	public AuthenticationProvider socialAuthenticationProvider(UserDetailsService userDetailsService) {
		return new SocialAuthenticationProvider(usersConnectionRepository, socialUsersDetailsService());
	}

	@Bean
	public SocialUserDetailsService socialUsersDetailsService() {
		return new SocialUserDetailsServiceImpl();
	}

	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationUserIdExtractor();
	}
	

}