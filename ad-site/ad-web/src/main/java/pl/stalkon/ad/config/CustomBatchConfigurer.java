package pl.stalkon.ad.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;


public class CustomBatchConfigurer  {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;

	private JobLauncher createJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}

	private JobRepository createJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		factory.afterPropertiesSet();
		return  (JobRepository) factory.getObject();
	}
	
	@Bean
	public JobRepository jobRepository() throws Exception {
		return createJobRepository();
	}

	@Bean
	public JobLauncher jobLauncher() throws Exception {
		return createJobLauncher();
	}
	
	@Bean
	public JobBuilderFactory jobBuilders() throws Exception {
		return new JobBuilderFactory(jobRepository());
	}

	@Bean
	public StepBuilderFactory stepBuilders() throws Exception {
		return new StepBuilderFactory(jobRepository(), transactionManager);
	}

}
