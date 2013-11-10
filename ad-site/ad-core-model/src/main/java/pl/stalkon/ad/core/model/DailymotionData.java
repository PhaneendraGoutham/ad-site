package pl.stalkon.ad.core.model;

import javax.validation.constraints.NotNull;

import pl.styall.library.core.model.CommonEntity;

//@Entity
//@Table(name="dailymotion")
public class DailymotionData extends CommonEntity {
	
	@NotNull
	private String videoId;

	@NotNull
	private String videoUrl;
	
	@NotNull
	private String mediumThumbnailUrl;
	
	public DailymotionData(String videoId, String videoUrl, String mediumThumbnailUrl) {
		super();
		this.videoId = videoId;
		this.videoUrl = videoUrl;
		this.mediumThumbnailUrl = mediumThumbnailUrl;
	}
	public DailymotionData(){
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getMediumThumbnailUrl() {
		return mediumThumbnailUrl;
	}
	public void setMediumThumbnailUrl(String mediumThumbnailUrl) {
		this.mediumThumbnailUrl = mediumThumbnailUrl;
	}

}
