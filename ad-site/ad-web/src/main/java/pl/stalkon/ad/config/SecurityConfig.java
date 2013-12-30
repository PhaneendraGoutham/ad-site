package pl.stalkon.ad.config;

import java.awt.Color;


import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

import pl.stalkon.ad.core.model.service.UserInfoService;
import pl.stalkon.ad.core.security.SocialLoggedUser;
import pl.stalkon.ad.core.security.UserStatusMapperImpl;
import pl.styall.library.core.security.authorization.LoggedUserPermissionEvaluator;
import pl.styall.library.core.security.filter.UserMessageFilter;
import pl.styall.library.core.security.rest.AuthenticationEntryPointImpl;
import pl.styall.library.core.security.rest.AuthenticationResultHandler;
import pl.styall.library.core.security.rest.HibernateTokenRepository;
import pl.styall.library.core.security.rest.LoginController;
import pl.styall.library.core.security.rest.SeriesTokenAuthenticationProvider;
import pl.styall.library.core.security.rest.TokenAuthenticationFilter;
import pl.styall.library.core.security.rest.TokenRepository;
import pl.styall.library.core.security.rest.TokenService;
import pl.styall.library.core.security.rest.TokenServiceImpl;
import pl.styall.library.core.security.rest.UpdateTokenResultHandler;
import pl.styall.library.core.security.rest.UserLoginsService;

@Configuration
@ImportResource("classpath:pl/stalkon/ad/config/security-context.xml")
public class SecurityConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Resource(name="userService")
	private UserLoginsService userLoginsService;
	
//	@Bean
//	public JdbcTokenRepositoryImpl tokenRepository() {
//		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
//		repository.setDataSource(dataSource);
//		repository.setCreateTableOnStartup(false);
//		return repository;
//	}
//
//	@Bean
//	public PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices() {
//		PersistentTokenBasedRememberMeServices service = new PersistentTokenBasedRememberMeServices(
//				env.getProperty("login.cookies.key"), userDetailsService,
//				tokenRepository());
//		service.setAlwaysRemember(true);
//		return service;
//	}
	@Bean
	public LoggedUserPermissionEvaluator<SocialLoggedUser> loggedUserPermissionEvaluator(){
		return new LoggedUserPermissionEvaluator<SocialLoggedUser>();
	}
	@Bean
	public LoginController loginController(){
		return new LoginController(tokenService(), new UserStatusMapperImpl());
	}
	
	@Bean
	public TokenRepository tokenRepository(){
		return new HibernateTokenRepository();
	}
	
	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint(){
		return new AuthenticationEntryPointImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public TokenService tokenService(){
		TokenServiceImpl tokenService = new TokenServiceImpl(tokenRepository(), userDetailsService, bCryptPasswordEncoder(), userLoginsService);
		return tokenService;
	}
	
	@Bean
	public AuthenticationResultHandler authenticationResultHandler(){
		UpdateTokenResultHandler handler = new UpdateTokenResultHandler(tokenService());
		return handler;
	}
	
	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter(){
		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter(authenticationManager,authenticationEntryPoint());
		tokenAuthenticationFilter.setAuthenticationResultHandler(authenticationResultHandler());
		return tokenAuthenticationFilter;
	}
	
	@Bean
	public UserMessageFilter userMessageFilter(){
		UserMessageFilter filter = new UserMessageFilter(userInfoService);
		filter.setExpires(5*60);
		return filter;
	}
	

	@Bean
	public AuthenticationProvider authenticationProvider() {
		return new SeriesTokenAuthenticationProvider(tokenService(), userDetailsService);
	}

	@Bean
	public GenericManageableCaptchaService captchaService(){
		
		RandomFontGenerator randomFontGenerator = new RandomFontGenerator(20, 30);
		BackgroundGenerator bg = new UniColorBackgroundGenerator(150, 35, new Color(181, 181, 181));
		Color color = new Color(0,0,0);
		SimpleTextPaster simpleTextPaster = new SimpleTextPaster(6, 10, color);
		WordToImage w2i = new ComposedWordToImage(randomFontGenerator, bg, simpleTextPaster);
		CaptchaFactory cf = new GimpyFactory(new DictionaryWordGenerator(new FileDictionary("toddlist")), w2i);
		GenericCaptchaEngine engine = new GenericCaptchaEngine(new CaptchaFactory[] {cf});
		return new GenericManageableCaptchaService(engine, 180, 180000);
	}
	

}
