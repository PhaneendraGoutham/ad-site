package pl.stalkon.ad.config;

import java.util.List;

import javax.inject.Inject;

import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.UnresolvingLocaleDefinitionsFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

import pl.stalkon.ad.core.interceptors.MiniBrowserInterceptor;
import pl.stalkon.ad.core.model.service.AdService;
import pl.styall.library.core.ext.QueryArgumentResolver;

@EnableWebMvc
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {
	
	@Inject
	private AdService adService;

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
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
		registry.addInterceptor(miniBrowserInterceptor).addPathPatterns("/ad/**", "/user/**");
	
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
	}
}
