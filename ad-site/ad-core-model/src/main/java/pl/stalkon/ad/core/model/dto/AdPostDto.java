package pl.stalkon.ad.core.model.dto;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Ad.Type;

public class AdPostDto {
	private String title;
	private String description;
	private String url;
	private Type type;
	private Long brandId;
	
	
	
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

}
