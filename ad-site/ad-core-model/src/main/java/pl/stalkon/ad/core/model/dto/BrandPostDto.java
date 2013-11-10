package pl.stalkon.ad.core.model.dto;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class BrandPostDto {
	@NotEmpty
	private String name;
	
	@Size(max=1024)
	private String description;
	
//	private CommonsMultipartFile logo;
//	public CommonsMultipartFile getLogo() {
//		return logo;
//	}
//	public void setLogo(CommonsMultipartFile logo) {
//		this.logo = logo;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
