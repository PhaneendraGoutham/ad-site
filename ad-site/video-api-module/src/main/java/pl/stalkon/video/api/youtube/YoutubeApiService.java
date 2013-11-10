package pl.stalkon.video.api.youtube;

import org.springframework.stereotype.Component;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.YoutubeVideoData;
import pl.stalkon.ad.core.model.dto.AdPostDto;

@Component
public class YoutubeApiService  {
	public Ad setVideoDetails(AdPostDto adPostDto)throws InvalidYoutubeUrlException {
		YoutubeVideoData video = new YoutubeVideoData();
		String youtubeId = getYoutubeVideoId(adPostDto.getUrl());
		Ad ad = null;
		if (!youtubeId.equals("")) {
			video.setVideoId(youtubeId);
			ad = new Ad();
			ad.setYoutubeVideoData(video);
		}
		return ad;
	}

	private String getYoutubeVideoId(String youtubeUrl) throws InvalidYoutubeUrlException {
		String video_id = "";
		if (youtubeUrl.startsWith("http://youtu.be/")
				|| youtubeUrl.startsWith("http://www.youtube.com/watch?v=")) {
			video_id = youtubeUrl.replace("http://youtu.be/", "");
			video_id = video_id.replace("http://www.youtube.com/watch?v=", "");
		}else{
			throw new InvalidYoutubeUrlException();
		}
		return video_id;
	}
}
