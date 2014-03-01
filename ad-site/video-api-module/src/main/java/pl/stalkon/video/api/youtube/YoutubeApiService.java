package pl.stalkon.video.api.youtube;

import org.springframework.stereotype.Component;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.VideoData;
import pl.stalkon.ad.core.model.VideoData.Provider;
import pl.stalkon.ad.core.model.dto.AdPostDto;

@Component
public class YoutubeApiService {
	public Ad setVideoDetails(AdPostDto adPostDto) {
		String youtubeId = getYoutubeVideoId(adPostDto.getUrl());
		VideoData video = new VideoData(youtubeId,Provider.YOUTUBE);
		Ad ad = new Ad();
		ad.setApproved(true);
		ad.setVideoData(video);
		return ad;
	}

	private String getYoutubeVideoId(String youtubeUrl) {
		String video_id = "";
		video_id = youtubeUrl.replace("http://youtu.be/", "");
		video_id = video_id.replace("http://www.youtube.com/watch?v=", "");
		return video_id;
	}
}
