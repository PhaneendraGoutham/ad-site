package pl.stalkon.ad.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "wistia_videos")
public class WistiaVideoData extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9048875841555670096L;
	private static final String VIDEO_URL_PREFIX = "http://fast.wistia.net/embed/iframe/";
	private static final String VIDEO_URL_OPTIONS = "?controlsVisibleOnLoad=true&version=v1&volumeControl=true";

	@Column(name = "video_id", nullable = false)
	private String videoId;

	@Column(nullable = false)
	private String thumbnail;

	@Column(nullable = false)
	private Long duration;

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public WistiaVideoData() {

	}

	public WistiaVideoData(String videoId, String thumbnail, Long duration) {
		this.videoId = videoId;
		this.thumbnail = thumbnail;
		this.duration = duration;
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

	public String getVideoUrl() {
		return VIDEO_URL_PREFIX + videoId + VIDEO_URL_OPTIONS;
	}

}
