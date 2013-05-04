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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.NotEmpty;

import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "ads")
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
	@Column(name="date_on_main")
	private Date dateOnMain;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name="creation_date")
	private Date creationDate;

	@Column(nullable=false)
	private Integer year;

	@Column(nullable = false, length = 128)
	private String title;

	@Column(nullable = false, length = 512)
	private String description;

	@Column(nullable = false, columnDefinition = "BOOL default false",name="age_protected")
	private Boolean ageProtected = false;

	@Column(nullable = false, columnDefinition = "BOOL default true")
	private Boolean approved = true;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "brand_id")
	private Brand brand;

//	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade=CascadeType.ALL)
//	@JoinTable(name="contest_ad", joinColumns={@JoinColumn(name="ad_id", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="id", referencedColumnName="id")})
	@OneToOne(optional=true, mappedBy="ad", cascade=CascadeType.ALL)
	private ContestAd contestAd;

	private Type contentType = Type.MOVIE;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
	private List<Rank> ranks;

	@Formula("(SELECT (SUM(r.rank)/COUNT(*)) from ranks as r where r.adId = id)")
	private Double rank;

	@Formula("(SELECT COUNT(*) from ranks as r where r.adId = id)")
	private Long voteCount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ad")
	@OrderBy(value = "date")
	private List<AdComment> comments = new ArrayList<AdComment>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional=true)
	@JoinColumn(name = "wistiaVideo")
	private WistiaVideo wistiaVideo;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional=true)
	@JoinColumn(name = "youtubeVideo")
	private YoutubeVideo youtubeVideo;;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ad_tags", joinColumns = { @JoinColumn(name = "ad_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") }, uniqueConstraints = { @UniqueConstraint(name = "ad_tag_unique", columnNames = {
			"ad_id", "tag_id" }) })
	private List<Tag> tags = new ArrayList<Tag>();

	private Long duration;

	@Column(nullable = false)
	private Boolean official;

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public String getThumbnail() {
		if (wistiaVideo != null) {
			return wistiaVideo.getThumbnail();
		} else {
			return youtubeVideo.getVideoThumnail();
		}
	}

	public ContestAd getContestAd() {
		return contestAd;
	}

	public void setContestAd(ContestAd contestAd) {
		this.contestAd = contestAd;
	}

	public String getVideoUrl() {
		if (wistiaVideo != null) {
			return wistiaVideo.getVideoUrl();
		} else {
			return youtubeVideo.getVideoUrl();
		}
	}

	public Double getRank() {
		if (rank != null) {
			return this.rank / voteCount;
		} else
			return new Double(0);
	}

	public void setRank(Double rank) {
		this.rank = rank;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public List<AdComment> getComments() {
		return comments;
	}

	public void setComments(List<AdComment> comments) {
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

	// public Contest getContest() {
	// return contest;
	// }
	//
	// public void setContest(Contest contest) {
	// this.contest = contest;
	// }

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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

	public YoutubeVideo getYoutubeVideo() {
		return youtubeVideo;
	}

	public void setYoutubeVideo(YoutubeVideo youtubeVideo) {
		this.youtubeVideo = youtubeVideo;
	}

	public Boolean getOfficial() {
		return official;
	}

	public void setOfficial(Boolean official) {
		this.official = official;
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

	public WistiaVideo getWistiaVideo() {
		return wistiaVideo;
	}

	public void setWistiaVideo(WistiaVideo wistiaVideo) {
		this.wistiaVideo = wistiaVideo;
	}

	public Date getDateOnMain() {
		return dateOnMain;
	}

	public void setDateOnMain(Date dateOnMain) {
		this.dateOnMain = dateOnMain;
	}

	// public DailymotionData getDailymotionData() {
	// return dailymotionData;
	// }
	//
	// public void setDailymotionData(DailymotionData dailymotionData) {
	// this.dailymotionData = dailymotionData;
	// }

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		tags.add(tag);
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(Long voteCount) {
		this.voteCount = voteCount;
	}

}
