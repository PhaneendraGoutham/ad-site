package pl.stalkon.ad.config;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;

import pl.stalkon.ad.core.interceptors.MiniBrowserInterceptor;
import pl.stalkon.ad.core.model.service.AdService;
import pl.styall.library.core.ext.QueryArgumentResolver;

@EnableWebMvc
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {
	
	@Inject
	private AdService adService;
	
	@Inject
	private Environment env;

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("pl"));
		return localeResolver;
	}
	
	
	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(new Long(env.getProperty("file.maxSize")));
		return commonsMultipartResolver;
	}

	// @Bean
	// public ViewResolver viewResolver() {
	// // TODO przemyslec poprawic
	// UrlBasedViewResolver resolver = new UrlBasedViewResolver();
	// resolver.setViewClass(TilesView.class);
	// resolver.setPrefix("/WEB-INF/views/");
	// resolver.setSuffix(".jsp");
	//
	// return resolver;
	// }
	// @Bean
	// public TilesConfigurer tilesConfigurer(){
	// TilesConfigurer conf = new TilesConfigurer();
	// conf.setDefinitionsFactoryClass(UnresolvingLocaleDefinitionsFactory.class);
	// conf.setDefinitions(new String[]{"/WEB-INF/views/tiles.xml"});
	// return conf;
	// }
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		MiniBrowserInterceptor	miniBrowserInterceptor = new MiniBrowserInterceptor();
		miniBrowserInterceptor.setAdService(adService);
		registry.addInterceptor(miniBrowserInterceptor).addPathPatterns("/**", "/**");
	}

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
	}
	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ServletWebArgumentResolverAdapter(
				new QueryArgumentResolver()));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/**");
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/favicon.ico");
	}
	

}
