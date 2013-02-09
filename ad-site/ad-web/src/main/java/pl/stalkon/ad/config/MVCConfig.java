package pl.stalkon.ad.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;


import pl.styall.library.core.ext.QueryArgumentResolver;
import pl.styall.scylla.json.config.CustomMappingJackson2;
import pl.styall.scylla.json.config.CustomObjectMapper;

@EnableWebMvc
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter {

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

	@Bean
	public ViewResolver viewResolver() {
		// TODO przemyslec poprawic
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);
		return resolver;
	}

	@Bean
	public CustomObjectMapper jacksonObjectMapper() {
		return new CustomObjectMapper();
	}

	@Bean
	public CustomMappingJackson2 mappingJackson2() {
		CustomMappingJackson2 customMappingJackson2 = new CustomMappingJackson2();
		customMappingJackson2.setObjectMapper(jacksonObjectMapper());
		return customMappingJackson2;
	}

	@Bean
	public AnnotationMethodHandlerExceptionResolver annotationMethodHandlerExceptionResolver() {
		AnnotationMethodHandlerExceptionResolver resolver = new AnnotationMethodHandlerExceptionResolver();
		HttpMessageConverter<?> converters[] = { mappingJackson2() };
		resolver.setMessageConverters(converters);
		return resolver;
	}

	@Override
	public void configureMessageConverters(
			List<HttpMessageConverter<?>> converters) {
		converters.add(mappingJackson2());
	}

	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ServletWebArgumentResolverAdapter(
				new QueryArgumentResolver()));
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
}
