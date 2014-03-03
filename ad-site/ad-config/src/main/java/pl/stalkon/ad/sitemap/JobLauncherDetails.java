package pl.stalkon.ad.sitemap;

import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class JobLauncherDetails extends QuartzJobBean {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job generateSitemap;

	@SuppressWarnings("unchecked")
	protected void executeInternal(JobExecutionContext context) {
		Map<String, Object> jobDataMap = context.getMergedJobDataMap();
		JobParameters jobParameters = getJobParametersFromJobMap(jobDataMap);
		try {
			jobLauncher.run(generateSitemap, jobParameters);
		} catch (JobExecutionException e) {
		}
	}

	private JobParameters getJobParametersFromJobMap(
			Map<String, Object> jobDataMap) {

		JobParametersBuilder builder = new JobParametersBuilder();

		for (Entry<String, Object> entry : jobDataMap.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof String) {
				builder.addString(key, (String) value);
			} else if (value instanceof Float || value instanceof Double) {
				builder.addDouble(key, ((Number) value).doubleValue());
			} else if (value instanceof Integer || value instanceof Long) {
				builder.addLong(key, ((Number) value).longValue());
			} else if (value instanceof Date) {
				builder.addDate(key, (Date) value);
			} else {
			}
		}
		builder.addDate("timestamp", new Date());
		return builder.toJobParameters();

	}

}