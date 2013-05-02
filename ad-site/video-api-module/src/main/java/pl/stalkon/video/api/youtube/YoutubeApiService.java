package pl.stalkon.video.api.youtube;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.YoutubeVideo;
import pl.stalkon.ad.core.model.dto.AdPostDto;

@Component
public class YoutubeApiService  {
	public Ad setVideoDetails(AdPostDto adPostDto)throws InvalidYoutubeUrlException {
		YoutubeVideo video = new YoutubeVideo();
		String youtubeId = getYoutubeVideoId(adPostDto.getUrl());
		Ad ad = null;
		if (!youtubeId.equals("")) {
			video.setVideoId(youtubeId);
			ad = new Ad();
			ad.setYoutubeVideo(video);
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
