package pl.stalkon.ad.config;

import java.util.Properties;


import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pl.stalkon.ad.core.model.SocialUser;
import pl.stalkon.ad.core.model.search.ext.IndexRebuilder;
import pl.stalkon.ad.core.model.service.impl.UserRolePopulator;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.dao.CriteriaConfigurer;
import pl.styall.library.core.model.defaultimpl.Address;
import pl.styall.library.core.model.defaultimpl.UserData;
import pl.styall.library.spring.DatabaseInitPopulator;

@Configuration
@EnableTransactionManagement(order = 2)
public class HibernateConfig {

	@Autowired
	private Environment env;

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName("com.mysql.jdbc.Driver");
		source.setUrl(env.getProperty("database.url")
				+ "?characterEncoding=UTF-8");
		source.setUsername(env.getProperty("database.username"));
		source.setPassword(env.getProperty("database.password"));
		return source;
	}

	@Bean
	public CriteriaConfigurer criteriaConfigurer() {
		return new CriteriaConfigurer();
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setPackagesToScan("pl.stalkon.ad.core.model", "pl.styall.library.core.security.rest");
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setAnnotatedClasses(new Class[] { UserData.class,
				Address.class, UserRole.class, SocialUser.class });
		Properties prop = new Properties();
		prop.setProperty("hibernate.dialect",
				"org.hibernate.dialect.MySQLDialect");
		prop.setProperty("hibernate.show_sql", "true");
		prop.setProperty("hibernate.hbm2ddl.auto", "update");
		prop.setProperty("connection.characterEncoding", "UTF-8");
		prop.setProperty("hibernate.connection.useUnicode", "yes");
		prop.setProperty("hibernate.hbm2ddl.import_files",
				new ClassPathResource("hibernate/persistent_logins.sql")
						.getPath());		
		prop.setProperty("hibernate.search.default.directory_provider", "filesystem");
		prop.setProperty("hibernate.search.default.indexBase",env.getProperty("app.files.root.directory") + env.getProperty("lucene.index.folder"));
		sessionFactory.setHibernateProperties(prop);
		return sessionFactory;
	}

	@Bean
	public HibernateTransactionManager transactionManager() {
		return new HibernateTransactionManager(sessionFactory().getObject());
	}
	
	@Bean
	public DatabaseInitPopulator databaseInitPopulator(){
		DatabaseInitPopulator databasePopulator = new DatabaseInitPopulator();
		databasePopulator.adPopulator(new UserRolePopulator());
		databasePopulator.adPopulator(new IndexRebuilder(new Boolean(env.getProperty("lucene.index.rebuild")), transactionManager().getSessionFactory()));
		return databasePopulator;
	}
	
}
