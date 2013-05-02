package pl.stalkon.ad.config;

import java.awt.Color;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

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
import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;

import pl.stalkon.ad.extensions.CaptchaValidator;
import pl.styall.library.core.security.authentication.AuthenticationProvider;

@Configuration
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
	
	@Bean
	public CaptchaValidator captchaValidator(){
		return new CaptchaValidator();
	}
	

}
