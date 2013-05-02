package pl.stalkon.ad.core.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="wistia_video")
public class WistiaVideo extends CommonEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9048875841555670096L;
	private static final String VIDEO_URL_PREFIX = "http://fast.wistia.net/embed/iframe/";
	private static final String VIDEO_URL_OPTIONS ="?controlsVisibleOnLoad=true&version=v1&volumeControl=true";

	@NotNull
	private String videoId;

	@NotNull
	private String thumbnail;
	
	
	
public WistiaVideo(){
	
}
	public WistiaVideo(String videoId, String thumbnail) {
		this.videoId = videoId;
		this.thumbnail = thumbnail;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getVideoUrl(){
		return VIDEO_URL_PREFIX + videoId + VIDEO_URL_OPTIONS;
	}
	
}
