package pl.stalkon.ad.rest.controller;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OtherController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job generateSitemap;

	@RequestMapping(value = "/generateSitemapAndPages", method = RequestMethod.GET)
	@ResponseBody
	public void generateSitemapAndPages() {
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addDate("timestamp", new Date());
		try {
			jobLauncher.run(generateSitemap, builder.toJobParameters());
		} catch (JobExecutionException e) {
		}
	}

}
