package pl.stalkon.ad.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = { "pl.stalkon.ad.core.controller",
		"pl.stalkon.ad.core.model",
		"pl.stalkon.dailymotion.api.module.service.impl"})
@PropertySource("classpath:pl/stalkon/ad/config/application.properties")
public class MainConfig {

}
