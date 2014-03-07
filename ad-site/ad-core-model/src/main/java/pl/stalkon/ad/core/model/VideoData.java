package pl.stalkon.ad.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "video_data")
public class VideoData extends CommonEntity {

	private static final long serialVersionUID = -2421274195822600142L;

	private static final String WISTIA_VIDEO_URL_PREFIX = "http://fast.wistia.net/embed/iframe/";
	private static final String WISTIA_VIDEO_URL_OPTIONS = "?autoPlay=true&controlsVisibleOnLoad=true&version=v1&volumeControl=true";
	private static final String WISTIA_THUMBNAIL_SUFFIX = "?image_crop_resized=100x60";
	private static final String YOUTUBE_THUMBNAIL_URL_PREFIX = "http://i.ytimg.com/vi/";
	private static final String YOUTUBE_THUMBNAIL_EXTENSION = ".jpg";
	private static final String YOUTUBE_THUMBNAIL_NUMBER = "default";
	private static final String YOUTUBE_VIDEO_URL_PREFIX = "http://www.youtube.com/embed/";
	private static final String YOUTUBE_EMBED_VIDEO_URL_PREFIX = "http://www.youtube.com/v/";
	private static final String YOUTUBE_EMBED_VIDEO_URL_SUFIX = "?version=3&amp;hl=pl_PL&amp;";
	private static final String YOUTUBE_IFRAME_VIDEO_URL_SUFIX = "?rel=0&autoplay=1";

	public enum Provider {
		WISTIA, YOUTUBE;
	}

	@Column(name = "video_id", nullable = false)
	private String videoId;

	private String thumbnail;

	private Double duration;

	@Column(name = "video_url")
	private String videoUrl;

	@Enumerated
	private Provider provider;

	public VideoData() {
	}

	public VideoData(String videoId, Provider provider) {
		this.videoId = videoId;
		this.provider = provider;
	}

	public VideoData(String videoId, String thumbnail, Double duration,
			String videoUrl, Provider provider) {
		this.videoId = videoId;
		this.thumbnail = thumbnail;
		this.duration = duration;
		this.videoUrl = videoUrl;
		this.provider = provider;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public String getBigThumbnail() {
		switch (provider) {
		case WISTIA:
			return thumbnail;
		case YOUTUBE:
			return YOUTUBE_THUMBNAIL_URL_PREFIX + videoId + "/" + "0"
					+ YOUTUBE_THUMBNAIL_EXTENSION;
		default:
			return "";
		}
	}

	public String getSmallThumbnail() {
		switch (provider) {
		case WISTIA:
			return thumbnail + WISTIA_THUMBNAIL_SUFFIX;
		case YOUTUBE:
			return YOUTUBE_THUMBNAIL_URL_PREFIX + videoId + "/"
					+ YOUTUBE_THUMBNAIL_NUMBER + YOUTUBE_THUMBNAIL_EXTENSION;
		default:
			return "";
		}
	}

	public String getIframeVideoUrl() {
		switch (provider) {
		case WISTIA:
			return WISTIA_VIDEO_URL_PREFIX + videoId + WISTIA_VIDEO_URL_OPTIONS;
		case YOUTUBE:
			return YOUTUBE_VIDEO_URL_PREFIX + videoId + YOUTUBE_IFRAME_VIDEO_URL_SUFIX;
		default:
			return "";
		}

	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}

	public String getVideoUrl() {
		switch (provider) {
		case WISTIA:
			return videoUrl;
		case YOUTUBE:
			return YOUTUBE_EMBED_VIDEO_URL_PREFIX + videoId
					+ YOUTUBE_EMBED_VIDEO_URL_SUFIX;
		default:
			return "";
		}

	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

}
