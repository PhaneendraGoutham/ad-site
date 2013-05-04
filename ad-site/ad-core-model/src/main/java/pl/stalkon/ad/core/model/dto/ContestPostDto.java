package pl.stalkon.ad.core.model.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import pl.stalkon.ad.core.model.Contest;

public class ContestPostDto {

	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	@NotNull
	private Contest.Type type;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date finishDate;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private Date scoresDate;
	
	private Long brandId;
	
	
	@NotNull
	private CommonsMultipartFile image;
	
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
	public Contest.Type getType() {
		return type;
	}
	public void setType(Contest.Type type) {
		this.type = type;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public Date getScoresDate() {
		return scoresDate;
	}
	public void setScoresDate(Date scoresDate) {
		this.scoresDate = scoresDate;
	}
	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	public CommonsMultipartFile getImage() {
		return image;
	}
	public void setImage(CommonsMultipartFile image) {
		this.image = image;
	}

}
