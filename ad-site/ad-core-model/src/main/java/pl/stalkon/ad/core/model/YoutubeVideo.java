package pl.stalkon.ad.core.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "youtube_video")
public class YoutubeVideo extends CommonEntity {
	
	private static final String THUMBNAIL_URL_PREFIX = "http://img.youtube.com/vi/";
	private static final String THUMBNAIL_EXTENSION = ".jpg";
	private static final int THUMBNAIL_NUMBER = 1;
	private static final String VIDEO_URL_PREFIX = "http://www.youtube.com/embed/";
	private String videoId;
	

	public String getVideoId() {
		return videoId;
	}


	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	
	public String getVideoThumnail(){
		return THUMBNAIL_URL_PREFIX + videoId + "/" + THUMBNAIL_NUMBER + THUMBNAIL_EXTENSION;
	}
	
	public String getVideoUrl(){
		return VIDEO_URL_PREFIX + videoId;
	}


}
