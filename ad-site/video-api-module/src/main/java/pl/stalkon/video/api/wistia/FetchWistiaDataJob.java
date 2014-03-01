package pl.stalkon.video.api.wistia;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.VideoData;
import pl.stalkon.ad.core.model.service.AdService;
import pl.stalkon.video.api.service.WistiaFetchVideoDataScheduler;
import pl.stalkon.video.api.service.impl.WistiaApiService;

@DisallowConcurrentExecution
public class FetchWistiaDataJob implements Job {

	public static final String WISTIA_VIDEO_ID = "WISTIA_VIDEO_ID";

	@Autowired
	private AdService adService;

	@Autowired
	private WistiaApiService wistiaApiService;

	@Autowired
	private WistiaFetchVideoDataScheduler fetchVideoDataScheduler;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap data = context.getJobDetail().getJobDataMap();
		String wistaiVideoId = data.getString(WISTIA_VIDEO_ID);
		Ad ad = adService.getByWistiaVideoId(wistaiVideoId);
		VideoData wistiaVideoData = wistiaApiService
				.getVideoEmbeddedData(ad);
		if (wistiaVideoData != null)
			adService.setVideoData(ad.getId(), wistiaVideoData);
		else
			fetchVideoDataScheduler.scheduleWistiaFetchVideoData(wistaiVideoId);
	}
}
