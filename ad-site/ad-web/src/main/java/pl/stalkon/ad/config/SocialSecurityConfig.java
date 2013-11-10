package pl.stalkon.ad.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.social.security.SocialAuthenticationServiceLocator;

import pl.stalkon.ad.core.security.MixUserDetailsService;
import pl.stalkon.ad.core.security.SocialUserDao;
import pl.stalkon.ad.core.security.SocialUserDetailsServiceImpl;
import pl.stalkon.social.singleconnection.SocialAuthenticationFilter;
import pl.stalkon.social.singleconnection.SocialAuthenticationProvider;
import pl.stalkon.social.singleconnection.impl.AuthenticationUserIdSource;
import pl.stalkon.social.singleconnection.interfaces.UserIdSource;
import pl.stalkon.social.singleconnection.interfaces.UsersConnectionRepository;


@Configuration
public class SocialSecurityConfig {

	@Autowired
	private Environment environment;

	@Autowired
	private UsersConnectionRepository usersConnectionRepository;

	@Bean 
	public SocialAuthenticationFilter socialAuthenticationFilter(AuthenticationManager authenticationManager, RememberMeServices rememberMeServices, SocialAuthenticationServiceLocator authenticationServiceLocator) {
		SocialAuthenticationFilter socialAuthenticationFilter = new SocialAuthenticationFilter(authenticationManager, userIdSource(), usersConnectionRepository, authenticationServiceLocator);
		socialAuthenticationFilter.setRememberMeServices(rememberMeServices);
		socialAuthenticationFilter.setFilterProcessesUrl("/social/login");
		return socialAuthenticationFilter;
	}

	@Bean
	public AuthenticationProvider socialAuthenticationProvider() {
		return new SocialAuthenticationProvider(usersConnectionRepository, socialUsersDetailsService());
	}

	@Bean(name="userDetailsService")
	public MixUserDetailsService socialUsersDetailsService() {
		return new SocialUserDetailsServiceImpl();
	}
	


	@Bean
	public SocialUserDao socialUserDao(){
		return new SocialUserDao();
	}
	
	@Bean
	public UserIdSource userIdSource() {
		return new AuthenticationUserIdSource();
	}
	

}