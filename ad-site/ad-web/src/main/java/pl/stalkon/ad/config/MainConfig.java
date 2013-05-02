package pl.stalkon.ad.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan(basePackages = { "pl.stalkon.ad.core.controller",
		"pl.stalkon.ad.core.model",
		"pl.stalkon.dailymotion.api.module.service.impl", "pl.stalkon.video.api.service.impl", "pl.stalkon.video.api.youtube"})
@PropertySource("classpath:pl/stalkon/ad/config/application.properties")
@EnableCaching(order=1)
public class MainConfig {

	
	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender javamailSender(){
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setDefaultEncoding("UTF-8");
		javaMailSenderImpl.setUsername(env.getProperty("mail.username"));
		javaMailSenderImpl.setPassword(env.getProperty("mail.password"));
		javaMailSenderImpl.setProtocol("smtps");
		Properties props = new Properties();
		
		 props.put("mail.smtp.ssl.enable", "true");
		 props.put("mail.transport.protocol", "smtps");
		 props.put("mail.debug", "true");
		 props.put("mail.smtps.host", env.getProperty("mail.host"));
		 props.put("mail.smtps.port", new Integer(env.getProperty("mail.port")));
		 props.put("mail.smtps.socketFactory.port", env.getProperty("mail.port"));
		 props.put("mail.smtps.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 props.put("mail.smtps.socketFactory.fallback", "false");
		 props.put("mail.smtps.auth", "true");
//		prop.put("mail.smtp.auth", "true");
//		prop.put("mail.smtp.starttls.enable", "true");
////		prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//		prop.put("mail.debug", "true");
		javaMailSenderImpl.setJavaMailProperties(props);
		return javaMailSenderImpl;
	}
	
	@Bean(name="alertMailMessage")
	public SimpleMailMessage alertMailMessage(){
		SimpleMailMessage smm =  new SimpleMailMessage();
		smm.setFrom(env.getProperty("mail.alert.from"));
		smm.setTo(env.getProperty("mail.alert.to"));
		smm.setSubject("Wystąpił błąd");
		return smm;
	}
	
	@Bean
    public CacheManager cacheManager() {
		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
		cacheManager.setCacheManager(ehCache().getObject());
        return cacheManager;
    }
	
	@Bean
	public EhCacheManagerFactoryBean ehCache(){
		EhCacheManagerFactoryBean ehCache = new EhCacheManagerFactoryBean();
		ehCache.setConfigLocation(new ClassPathResource("pl/stalkon/ad/config/ehcache.xml"));
		return ehCache;
	}

	
}
