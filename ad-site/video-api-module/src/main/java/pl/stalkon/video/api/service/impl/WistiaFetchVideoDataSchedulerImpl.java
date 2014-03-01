package pl.stalkon.video.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Service;

import pl.stalkon.video.api.service.WistiaFetchVideoDataScheduler;
import pl.stalkon.video.api.wistia.FetchWistiaDataJob;

@Service
public class WistiaFetchVideoDataSchedulerImpl implements WistiaFetchVideoDataScheduler {

	@Autowired
	private Scheduler scheduler;
	
	private int delay = 15;
	
	@Override
	public void scheduleWistiaFetchVideoData(String videoId) {
		JobDetailFactoryBean jdfb = new JobDetailFactoryBean();
		jdfb.setGroup("fetchWistiaVideoData");
		jdfb.setName("fetchWistiaVideoData_" + videoId);
		jdfb.setJobClass(FetchWistiaDataJob.class);
		Map<String, Object> jobDataMap = new HashMap<String, Object>(1);
		jobDataMap.put(FetchWistiaDataJob.WISTIA_VIDEO_ID, videoId);
		jdfb.setJobDataAsMap(jobDataMap);
		jdfb.afterPropertiesSet();
		Trigger simpleTrigger = TriggerBuilder
				.newTrigger()
				.withIdentity("fetchWistiaVideoDataTrigger"
						+ videoId, "fetchWistiaVideoData")
				.startAt(new Date(System.currentTimeMillis()+delay*60*1000))
				.build();
		try {
			scheduler.scheduleJob(jdfb.getObject(), simpleTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}


}
