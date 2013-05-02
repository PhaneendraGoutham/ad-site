package pl.stalkon.ad.core.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.OrderBy;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import org.apache.avro.Schema.Field.Order;
import org.hibernate.validator.constraints.NotEmpty;

import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.dto.validation.groups.AdMainField;
import pl.stalkon.ad.core.model.Tag;

public class AdSearchDto {
	
	private Integer year;
	private Double rankFrom;
	private Double rankTo;
	private Long votesFrom;
	private Long votesTo;
	private String text;
	private Short place;
	private List<Long> tagList;
	private List<Long> brandList;
	private String orderBy = "rank";
	private String order = "desc";


	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<Long> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<Long> brandList) {
		this.brandList = brandList;
	}

	public List<Long> getTagList() {
		return tagList;
	}

	public void setTagList(List<Long> tagList) {
		this.tagList = tagList;
	}

	public Integer getYear() {
		return year;
	}

	public Integer getYearFrom() {
		if (year == null ||year == 1) {
			return null;
		}
		return year;
	}

	public Integer getYearTo() {
		if (year == null || year == 1) {
			return null;
		}
		if (year < 2000) {
			return year + 10;
		} else {
			return year;
		}
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getRankFrom() {
		return rankFrom;
	}

	public void setRankFrom(Double rankFrom) {
		if (rankFrom == 0)
			this.rankFrom = null;
		else
			this.rankFrom = rankFrom;
	}

	public Double getRankTo() {
		return rankTo;
	}

	public void setRankTo(Double rankTo) {
		if (rankTo == 0)
			this.rankTo = null;
		else
			this.rankTo = rankTo;
	}

	public Long getVotesFrom() {
		return votesFrom;
	}

	public void setVotesFrom(Long votesFrom) {
		this.votesFrom = votesFrom;
	}

	public Long getVotesTo() {
		return votesTo;
	}

	public void setVotesTo(Long votesTo) {
		this.votesTo = votesTo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Short getPlace() {
		return place;
	}

	public void setPlace(Short place) {
		this.place = place;
	}


}
