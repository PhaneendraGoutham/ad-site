package pl.stalkon.ad.core.model.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.jobs.ContestFinishJob;
import pl.stalkon.ad.core.model.service.ContestFinishScheduler;

@Service
public class ContestFinishSchedulerImpl implements ContestFinishScheduler {

	@Autowired
	private Scheduler scheduler;

	@Override
	public void scheduleContestFinish(Contest contest) {
		
		JobDetailFactoryBean jdfb = new JobDetailFactoryBean();
		jdfb.setGroup("contestFinish");
		jdfb.setName("contestFinishJob_" + contest.getId());
		jdfb.setJobClass(ContestFinishJob.class);
		Map<String, Object> jobDataMap = new HashMap<String, Object>(1);
		jobDataMap.put(ContestFinishJob.CONTEST_ID, contest.getId());
		jdfb.setJobDataAsMap(jobDataMap);
		jdfb.afterPropertiesSet();
		Trigger simpleTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("contestFinishTrigger"
						+ contest.getId(), "contestFinish")
				.startAt(contest.getFinishDate())
				.build();
		try {
			scheduler.scheduleJob(jdfb.getObject(), simpleTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
