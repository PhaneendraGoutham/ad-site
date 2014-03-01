package pl.stalkon.ad.config;

import java.util.List;
import java.util.Locale;

import org.hibernate.engine.spi.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.ad.core.model.service.FileService;
import pl.stalkon.ad.core.model.service.impl.FileServiceImpl;
import pl.styall.library.core.rest.ext.EntityDtmMapper;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@EnableWebMvc
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private AdService adService;

	@Autowired
	private Environment env;
	@Autowired
	private LocalSessionFactoryBean sessionFactory;

	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable("default");
	}

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
	public EntityDtmMapper entityDtmMapper() {
		return new EntityDtmMapper();
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setMaxUploadSize(new Long(env
				.getProperty("file.maxSize")));
		return commonsMultipartResolver;
	}

	@Bean
	public FileService fileService() {
		String uploadsFolder = env.getProperty("upload.folder");
		boolean starts = uploadsFolder != null && uploadsFolder.startsWith("/");
		Assert.state(starts, "Upload folder if not empty must starts with /");
		String uploadsRootDirectory = env
				.getProperty("app.files.root.directory");
		boolean ends = uploadsRootDirectory != null
				&& uploadsRootDirectory.endsWith("/");
		Assert.state(!ends, "Uploads root path can't end with /");
		return new FileServiceImpl(uploadsRootDirectory, uploadsFolder);
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
		// MiniBrowserInterceptor miniBrowserInterceptor = new
		// MiniBrowserInterceptor();
		// miniBrowserInterceptor.setAdService(adService);
		WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
		webContentInterceptor.setCacheSeconds(862340);
		webContentInterceptor.setUseExpiresHeader(true);
		webContentInterceptor.setUseCacheControlHeader(true);
		webContentInterceptor.setUseCacheControlNoStore(true);
//		registry.addInterceptor(webContentInterceptor).addPathPatterns(
//				"/resources/**");
		// registry.addInterceptor(miniBrowserInterceptor).addPathPatterns("/**");
	}

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		Mapping mapping = sessionFactory.getConfiguration().buildMapping();
		objectMapper
				.registerModule(new Hibernate4Module(mapping)
						.configure(
								com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS,
								true));
		jacksonConverter.setObjectMapper(objectMapper);
		converters.add(jacksonConverter);
	}

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		// argumentResolvers.add(new ServletWebArgumentResolverAdapter(
		// new QueryArgumentResolver()));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations(
				"/resources/**");
		registry.addResourceHandler("/favicon.ico").addResourceLocations(
				"/favicon.ico");
		String rootPath = env.getProperty("app.files.root.directory");
		rootPath = rootPath.startsWith("/") ? rootPath.substring(1) : rootPath;
		registry.addResourceHandler("/uploads/**").addResourceLocations(
				"file:/" + rootPath + env.getProperty("upload.folder") + "/**");
	}

}
