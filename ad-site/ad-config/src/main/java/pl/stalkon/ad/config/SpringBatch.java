package pl.stalkon.ad.config;

import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestTemplate;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.sitemap.AdSitemapWriter;
import pl.stalkon.ad.sitemap.BrandSitemapWriter;
import pl.stalkon.ad.sitemap.MovePagesFilesTasklet;
import pl.stalkon.ad.sitemap.PhantomJSCaller_toDelete2;
import pl.stalkon.ad.sitemap.PhantomJSCaller;
import pl.stalkon.ad.sitemap.PhantomJsCloserTasklet;
import pl.stalkon.ad.sitemap.ContestSitemapWriter;
import pl.stalkon.ad.sitemap.PhantomJsRunnerTasklet;
import pl.stalkon.ad.sitemap.SendEmailOnFailureListener;
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

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ServletContext servletContext;

	@Bean
	public HibernateCursorItemReader<Ad> adCursorReader() {
		HibernateCursorItemReader<Ad> cr = new HibernateCursorItemReader<Ad>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Ad where approved = true");
		// cr.setUseStatelessSession(true);
		cr.setUseStatelessSession(false);
		return cr;
	}

	// @Bean
	// public HibernateCursorItemReader<Long> adIdCursorReader() {
	// HibernateCursorItemReader<Long> cr = new
	// HibernateCursorItemReader<Long>();
	// cr.setSessionFactory(sessionFactory);
	// cr.setQueryString("select id from Ad where approved = true");
	// // cr.setUseStatelessSession(true);
	// cr.setUseStatelessSession(true);
	// return cr;
	// }

	// @Bean
	// public HibernateCursorItemReader<User> userCursorReader() {
	// HibernateCursorItemReader<User> cr = new
	// HibernateCursorItemReader<User>();
	// cr.setSessionFactory(sessionFactory);
	// cr.setQueryString("from User");
	// cr.setUseStatelessSession(true);
	// return cr;
	// }

	@Bean
	public HibernateCursorItemReader<Brand> brandCursorReader() {
		HibernateCursorItemReader<Brand> cr = new HibernateCursorItemReader<Brand>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Brand");
		cr.setUseStatelessSession(true);
		return cr;
	}

	// @Bean
	// public HibernateCursorItemReader<Long> brandIdCursorReader() {
	// HibernateCursorItemReader<Long> cr = new
	// HibernateCursorItemReader<Long>();
	// cr.setSessionFactory(sessionFactory);
	// cr.setQueryString("select id from Brand");
	// cr.setUseStatelessSession(true);
	// return cr;
	// }

	@Bean
	public HibernateCursorItemReader<Contest> contestCursorReader() {
		HibernateCursorItemReader<Contest> cr = new HibernateCursorItemReader<Contest>();
		cr.setSessionFactory(sessionFactory);
		cr.setQueryString("from Contest");
		cr.setUseStatelessSession(true);
		return cr;
	}

	//
	// @Bean
	// public HibernateCursorItemReader<Long> contestIdCursorReader() {
	// HibernateCursorItemReader<Long> cr = new
	// HibernateCursorItemReader<Long>();
	// cr.setSessionFactory(sessionFactory);
	// cr.setQueryString("select id from Contest");
	// cr.setUseStatelessSession(true);
	// return cr;
	// }

	@Bean
	public PhantomJSCaller phantomJSCaller() {
		return new PhantomJSCaller();
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

	// @Bean
	// public UserSitemapWriter userSitemapWriter() {
	// return new UserSitemapWriter();
	// }

	@Bean
	public WriteXmlTasklet writeXmlTasklet() {
		return new WriteXmlTasklet();
	}

	@Bean
	public StaticSitemapWriterTasklet staticSitemapWriterTasklet() {
		return new StaticSitemapWriterTasklet();
	}
	
	@Bean
	public MovePagesFilesTasklet movePagesFilesTasklet(){
		return new MovePagesFilesTasklet(env.getProperty("phantomjs.pagesFilesDirectory"),  env.getProperty("currentPagesFilesDirectory"));
	}

	@Bean
	public PhantomJsRunnerTasklet phantomJsRunnerTasklet() {
		return new PhantomJsRunnerTasklet(
				env.getProperty("phantomjs.absolutePath"),
				env.getProperty("phantomjs.pagesFilesDirectory"),
				env.getProperty("phantomjs.angularServer.absolutePath"),
				env.getProperty("phantomjs.log"));
	}

	@Bean
	public PhantomJsCloserTasklet phantomJsCloserTasklet() {
		return new PhantomJsCloserTasklet();
	}

	@Bean
	public Job generateSitemap() {
		return jobs.get("generateSitemap")
				.listener(sendEmailOnFailureListener())
				.start(runPhantomJSStep())
				.next(staticSitemapWriteStep()).next(adSitemapStep())
				.next(brandSitemapStep()).next(contestSitemapStep())
				.next(writeXmlSitemapStep()).next(closePhantomJSStep()).next(movePagesFilesStep()).build();
	}

	@Bean
	public JobExecutionListener sendEmailOnFailureListener() {
		return new SendEmailOnFailureListener();
	}

	// @Bean
	// public Job generatePagesFiles() {
	// return
	// jobs.get("generatePagesFiles").listener(sendEmailOnFailureListener()).start(runPhantomJSStep())
	// .next(generateAdPagesFilesStep())
	// .next(generateBrandsPagesFilesStep())
	// .next(generateContestsPagesFilesStep())
	// .next(closePhantomJSStep()).build();
	//
	// }

	@Bean
	protected Step adSitemapStep() {
		return steps.get("adSitemapStep").<Ad, Ad> chunk(50)
				.reader(adCursorReader()).writer(adSitemapWriter()).build();
	}

	// @Bean
	// protected Step userSitemapStep() {
	// return steps.get("userSitemapStep").<User, User> chunk(100)
	// .reader(userCursorReader()).writer(userSitemapWriter()).build();
	// }

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
		return steps.get("staticSitemapStep")
				.tasklet(staticSitemapWriterTasklet()).build();
	}

	@Bean
	protected Step runPhantomJSStep() {
		return steps.get("runPhantomJSStep").tasklet(phantomJsRunnerTasklet())
				.build();
	}

	@Bean
	protected Step closePhantomJSStep() {
		return steps.get("closePhantomJSStep").allowStartIfComplete(true)
				.tasklet(phantomJsCloserTasklet()).build();
	}
	
	@Bean
	protected Step movePagesFilesStep(){
		return steps.get("movePagesFilesTasklet").tasklet(movePagesFilesTasklet()).build();
	}

	// @Bean
	// protected Step generateAdPagesFilesStep() {
	// return steps.get("adPagesFilesStep").<Long, Long> chunk(100)
	// .reader(adIdCursorReader())
	// .writer(new PhantomJSCaller_toDelete2("reklamy")).build();
	// }

	// @Bean
	// protected Step generateContestsPagesFilesStep() {
	// return steps.get("contestsPagesFilesStep").<Long, Long> chunk(100)
	// .reader(contestIdCursorReader())
	// .writer(new PhantomJSCaller_toDelete2("konkursy")).build();
	// }
	//
	// @Bean
	// protected Step generateBrandsPagesFilesStep() {
	// return steps.get("brandsPagesFilesStep").<Long, Long> chunk(100)
	// .reader(brandIdCursorReader())
	// .writer(new PhantomJSCaller_toDelete2("marki")).build();
	// }

}
