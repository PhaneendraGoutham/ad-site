package pl.stalkon.ad.config;

import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pl.styall.library.core.model.dao.CriteriaConfigurer;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

	@Inject
	private Environment env;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl(env.getProperty("database.url"));
		source.setUsername(env.getProperty("database.username"));
		source.setPassword(env.getProperty("database.password"));
		return source;
	}

	@Bean
	public CriteriaConfigurer criteriaConfigurer() {
		return new CriteriaConfigurer();
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory(){
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setPackagesToScan("pl.styall.library.core.model.defaultimpl", "pl.styall.library.core.model","pl.stalkon.ad.core.model", "pl.stalkon.social.model" );
		sessionFactory.setDataSource(dataSource());
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.hbm2ddl.auto", "update");
		prop.setProperty("connection.characterEncoding", "UTF-8");
		sessionFactory.setHibernateProperties(prop);
		return sessionFactory;
	}
	
	@Bean
	public HibernateTransactionManager transactionManager(){
		return new HibernateTransactionManager(sessionFactory().getObject());
	}
}
