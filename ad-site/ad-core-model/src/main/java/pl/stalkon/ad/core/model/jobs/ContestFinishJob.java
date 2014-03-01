package pl.stalkon.ad.core.model.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.service.ContestService;


@DisallowConcurrentExecution
public class ContestFinishJob implements Job {

	public static final String CONTEST_ID = "contestId";

	@Autowired
	private ContestService contestService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		Long contestId = data.getLong(CONTEST_ID);
		contestService.finish(contestId);
	}

}
