package pl.stalkon.ad.config;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.sitemap.AdSitemapWriter;
import pl.stalkon.ad.sitemap.BrandSitemapWriter;
import pl.stalkon.ad.sitemap.ContestSitemapWriter;
import pl.stalkon.ad.sitemap.StaticSitemapWriterTasklet;
import pl.stalkon.ad.sitemap.UserSitemapWriter;
import pl.stalkon.ad.sitemap.WriteXmlTasklet;

@Configuration
public class SpringBatch extends CustomBatchConfigurer {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private Environment env;

	@Autowired
	private SessionFactory sessionFactory;

	@Bean
	public HibernateCursorItemReader<Ad> adCursorReader() {
		HibernateCursorItemReader<Ad> cr = new HibernateCursorItemReader<Ad>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Ad where approved = true");
		// cr.setUseStatelessSession(true);
		cr.setUseStatelessSession(false);
		return cr;
	}

	@Bean
	public HibernateCursorItemReader<User> userCursorReader() {
		HibernateCursorItemReader<User> cr = new HibernateCursorItemReader<User>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from User");
		cr.setUseStatelessSession(true);
		return cr;
	}

	@Bean
	public HibernateCursorItemReader<Brand> brandCursorReader() {
		HibernateCursorItemReader<Brand> cr = new HibernateCursorItemReader<Brand>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Brand");
		cr.setUseStatelessSession(true);
		return cr;
	}

	@Bean
	public HibernateCursorItemReader<Contest> contestCursorReader() {
		HibernateCursorItemReader<Contest> cr = new HibernateCursorItemReader<Contest>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Contest");
		cr.setUseStatelessSession(true);
		return cr;
	}

	@Bean
	public AdSitemapWriter adSitemapWriter() {
		return new AdSitemapWriter();
	}

	@Bean
	public ContestSitemapWriter contestSitemapWriter() {
		return new ContestSitemapWriter();
	}

	@Bean
	public BrandSitemapWriter brandSitemapWriter() {
		return new BrandSitemapWriter();
	}

	@Bean
	public UserSitemapWriter userSitemapWriter() {
		return new UserSitemapWriter();
	}

	@Bean
	public WriteXmlTasklet writeXmlTasklet() {
		return new WriteXmlTasklet();
	}
	
	@Bean
	public StaticSitemapWriterTasklet staticSitemapWriterTasklet() {
		return new StaticSitemapWriterTasklet();
	}

	@Bean
	public Job generateSitemap() {
		return jobs.get("generateSitemap").start(staticSitemapWriteStep()).next(adSitemapStep())
				.next(userSitemapStep()).next(brandSitemapStep())
				.next(contestSitemapStep()).next(writeXmlSitemapStep()).build();
	}

	@Bean
	protected Step adSitemapStep() {
		return steps.get("adSitemapStep").<Ad, Ad> chunk(50)
				.reader(adCursorReader()).writer(adSitemapWriter()).build();
	}

	@Bean
	protected Step userSitemapStep() {
		return steps.get("userSitemapStep").<User, User> chunk(100)
				.reader(userCursorReader()).writer(userSitemapWriter()).build();
	}

	@Bean
	protected Step contestSitemapStep() {
		return steps.get("contestSitemapStep").<Contest, Contest> chunk(100)
				.reader(contestCursorReader()).writer(contestSitemapWriter())
				.build();
	}

	@Bean
	protected Step brandSitemapStep() {
		return steps.get("brandSitemapStep").<Brand, Brand> chunk(100)
				.reader(brandCursorReader()).writer(brandSitemapWriter())
				.build();
	}

	@Bean
	protected Step writeXmlSitemapStep() {
		return steps.get("writeXmlSitemap").tasklet(writeXmlTasklet()).build();
	}
	@Bean
	protected Step staticSitemapWriteStep() {
		return steps.get("staticSitemapStep").tasklet(staticSitemapWriterTasklet()).build();
	}

}
