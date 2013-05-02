package pl.stalkon.ad.core.model.dto;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Ad.Type;

public class AdPostDto {
	
	@NotEmpty
	@Size(max=60, min=3)
	private String title;
	
	@Size(max=500, min=3)
	private String description;
	
	@URL
	private String url;
	private Type type;
	
	@NotNull
	private Long brandId;
	
	private String thumbnail;
	private Long duration;
	private String videoId;
	
	@NotNull
	private Integer year;
	
	@NotNull
	private boolean ageProtected;
	


	@NotEmpty
	private List<Long> tags;
	
	
	
	
	public boolean isAgeProtected() {
		return ageProtected;
	}

	public void setAgeProtected(boolean ageProtected) {
		this.ageProtected = ageProtected;
	}
	
	public List<Long> getTags() {
		return tags;
	}

	public void setTags(List<Long> tags) {
		this.tags = tags;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
