package pl.stalkon.ad.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

import pl.styall.library.core.security.authentication.AuthenticationProvider;
import pl.styall.library.core.security.authorization.JacksonAccessDeniedHandler;
import pl.styall.library.core.security.authorization.RestAuthenticationEntryPoint;

@Configuration
@ComponentScan(basePackages = {
		"pl.styall.library.core.security.authentication",
		"pl.styall.library.core.security.ajax" })
@ImportResource("classpath:pl/stalkon/ad/config/security-context.xml")
public class SecurityConfig {

	@Inject
	private DataSource dataSource;

	@Inject
	private Environment env;
	
	@Inject
	private UserDetailsService userDetailsService;

	@Bean
	public JdbcTokenRepositoryImpl tokenRepository() {
		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
		repository.setDataSource(dataSource);
		repository.setCreateTableOnStartup(false);
		return repository;
	}

	@Bean
	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
		PersistentTokenBasedRememberMeServices service = new PersistentTokenBasedRememberMeServices(
				env.getProperty("login.cookies.key"), userDetailsService,
				tokenRepository());
		service.setAlwaysRemember(true);
		return service;
	}

	@Bean
	public ShaPasswordEncoder passwordEncoder() {
		return new ShaPasswordEncoder(256);
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new AuthenticationProvider();
	}

	@Bean
	public JacksonAccessDeniedHandler jacksonAccessDeniedHandler() {
		return new JacksonAccessDeniedHandler();
	}

	@Bean
	public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
		return new RestAuthenticationEntryPoint();
	}

}
