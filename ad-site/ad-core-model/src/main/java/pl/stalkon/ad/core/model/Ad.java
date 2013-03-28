package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;

import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "ad")
// @NamedNativeQuery(name = "rankCount", query =
// "SELECT COUNT(rank) from rank where adId = ?")
@FilterDef(name = "showApproved", defaultCondition = "approved = true")
public class Ad extends CommonEntity {

	private static final long serialVersionUID = 1335218634734582331L;

	public enum Type {
		MOVIE, PICTURE, GAME
	}

	public enum Place {
		MAIN, WAITING
	};

	@Column(nullable = false)
	private Place place = Place.WAITING;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateOnMain;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationDate;

	private String title;
	private String description;
	private Boolean ageProtected = false;

	private Boolean approved = true;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User poster;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "brandId")
	private Brand brand;

	private Type contentType = Type.MOVIE;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
	private List<Rank> ranks;

	@Formula("(SELECT SUM(r.rank)/COUNT(*) from ranks as r where r.adId = id)")
	private Double rank;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ad")
	private Set<AdComment> comments = new HashSet<AdComment>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "dailymiotionDataId")
	private DailymotionData dailymotionData;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Tag> tags = new ArrayList<Tag>();

	
	public void update(AdPostDto adPostDto, List<Tag> tags, Brand brand){
		this.setTitle(adPostDto.getTitle());
		this.setDescription(adPostDto.getDescription());
		this.setContentType(adPostDto.getType());
		if(tags != null)
		this.setTags(tags);
		this.setBrand(brand);
	}
	

	public Double getRank() {
		if(rank == null)
			return new Double(0);
		return rank;
	}

	public void setRank(Double rank) {
		this.rank = rank;
	}

	public User getPoster() {
		return poster;
	}

	public void setPoster(User poster) {
		this.poster = poster;
	}

	public void setContentType(Type contentType) {
		this.contentType = contentType;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public List<Rank> getRanks() {
		return ranks;
	}

	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}

	public void addRank(Rank rank) {
		rank.setAd(this);
		if (ranks == null) {
			ranks = new ArrayList<Rank>();
		}
		ranks.add(rank);
	}

	public Set<AdComment> getComments() {
		return comments;
	}

	public void setComments(Set<AdComment> comments) {
		this.comments = comments;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTitle() {
		return title;
	}

	public Boolean getApproved() {
		return approved;
	}

	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Type getContentType() {
		return contentType;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getAgeProtected() {
		return ageProtected;
	}

	public void setAgeProtected(Boolean ageProtected) {
		this.ageProtected = ageProtected;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Date getDateOnMain() {
		return dateOnMain;
	}

	public void setDateOnMain(Date dateOnMain) {
		this.dateOnMain = dateOnMain;
	}

	public DailymotionData getDailymotionData() {
		return dailymotionData;
	}

	public void setDailymotionData(DailymotionData dailymotionData) {
		this.dailymotionData = dailymotionData;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		tags.add(tag);
	}

}
