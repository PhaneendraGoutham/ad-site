package pl.stalkon.ad.core.model.dto;

import java.util.ArrayList;
import java.util.List;

import pl.stalkon.ad.core.model.Ad.Place;

public class AdSearchDto {
	
	private Integer yearFrom;
	private Integer yearTo;
	private Double rankFrom;
	private Double rankTo;
	private Long votesFrom;
	private Long votesTo;
	private String text;
	private Short place;
	private List<Long> tagList;
	private List<Long> brandList;
	private String orderBy = "creationDate";
	private String order = "desc";
	private Long contestId;
	private Boolean winner;
	private Long userId;
	private Integer page = 1;
	private Long parentId;
	private String search;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

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

//	public Integer getYearFrom() {
////		if (year == null ||year == 1) {
////			return null;
////		}
//		return yearFrom;
//	}
//
//	public Integer getYearTo() {
////		if (year == null || year == 1) {
////			return null;
////		}
////		if (year < 2000) {
////			return year + 10;
////		} else {
////			return year;
////		}
//		return yearTo;
//	}
//
//	public void setYear(Integer year) {
//		this.year = year;
//	}

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
	
	public void setPlace(Place place){
		this.place = (short) place.ordinal();
	}

	public Long getContestId() {
		return contestId;
	}

	public void setContestId(Long contestId) {
		this.contestId = contestId;
	}

	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(Integer yearFrom) {
		this.yearFrom = yearFrom;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public void setYearTo(Integer yearTo) {
		this.yearTo = yearTo;
	}

	public void setBrandId(Long id){
		this.brandList = new ArrayList<Long>(1);
		this.brandList.add(id);
	}

}
