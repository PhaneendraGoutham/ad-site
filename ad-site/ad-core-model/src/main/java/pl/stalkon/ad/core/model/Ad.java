package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.NGramFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.apache.solr.analysis.StempelPolishStemFilterFactory;
import org.apache.solr.analysis.StopFilterFactory;
import org.hibernate.annotations.Formula;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import pl.stalkon.ad.core.model.dto.VideoData;
import pl.stalkon.ad.core.model.dto.VideoData.Provider;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "ads")
@Indexed
@AnalyzerDefs({
		@AnalyzerDef(name = "polishAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = StempelPolishStemFilterFactory.class) }),
		@AnalyzerDef(name = "nGramAnalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
				@TokenFilterDef(factory = LowerCaseFilterFactory.class),
				@TokenFilterDef(factory = StopFilterFactory.class, params = {
						@Parameter(name = "words", value = "spring-config/stopwords.txt"),
						@Parameter(name = "ignoreCase", value = "true") }),
				@TokenFilterDef(factory = NGramFilterFactory.class, params = {
						@Parameter(name = "minGramSize", value = "3"),
						@Parameter(name = "maxGramSize", value = "10") }) }) })
public class Ad extends CommonEntity {

	private static final long serialVersionUID = 1335218634734582331L;

	public static final List<String> JSON_SHOW = Arrays.asList("id", "place",
			"dateOnMain", "creationDate", "year", "title", "description",
			"ageProtected", "approved", "user.displayName", "user.id",
			"user.userData.imageUrl", "brand.name", "brand.id",
			"brand.smallLogoUrl", "rank", "voteCount", "thumbnail", "videoUrl",
			"contestAd.winner", "official", "parentId",
			"videoData.provider.name", "videoData.provider.thumbnailUrl",
			"videoData.videoId");

	public enum Type {
		MOVIE, PICTURE, GAME
	}

	public enum Place {
		MAIN, WAITING
	};

	@Column(nullable = false)
	private Place place = Place.WAITING;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_on_main")
	private Date dateOnMain;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "creation_date")
	private Date creationDate;

	@Column(nullable = false)
	private Integer year;

	@Column(nullable = false, length = 128)
	@Field
	@Analyzer(definition = "nGramAnalyzer")
	private String title;

	@Column(length = 512)
	@Field
	@Analyzer(definition = "polishAnalyzer")
	private String description;

	@Column(nullable = false, columnDefinition = "BOOL default false", name = "age_protected")
	private Boolean ageProtected;

	@Column(nullable = false, columnDefinition = "BOOL default true")
	private Boolean approved;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "brand_id")
	@IndexedEmbedded
	private Brand brand;

	// @ManyToOne(fetch = FetchType.LAZY, optional = true,
	// cascade=CascadeType.ALL)
	// @JoinTable(name="contest_ad", joinColumns={@JoinColumn(name="ad_id",
	// referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="id",
	// referencedColumnName="id")})
	@OneToOne(optional = true, mappedBy = "ad", cascade = CascadeType.ALL)
	private ContestAd contestAd;

	@Column(name = "content_type", nullable = false)
	private Type contentType = Type.MOVIE;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "ad")
	private List<Rank> ranks;

	@Formula("(SELECT (SUM(r.rank)/COUNT(*)) from ranks as r where r.ad_id = id)")
	private Double rank;

	@Formula("(SELECT COUNT(*) from ranks as r where r.ad_id = id)")
	private Long voteCount;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ad")
	@OrderBy(value = "creation_date")
	private List<AdComment> comments;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "wistia_video_data_id")
	private WistiaVideoData wistiaVideoData;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "youtube_video_data_id")
	private YoutubeVideoData youtubeVideoData;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ad_tag_maps", joinColumns = { @JoinColumn(name = "ad_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") }, uniqueConstraints = { @UniqueConstraint(name = "ad_tag_uq", columnNames = {
			"ad_id", "tag_id" }) })
	private List<Tag> tags = new ArrayList<Tag>();

	@Column(nullable = false, columnDefinition = "BOOL default false")
	private Boolean official;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	private Ad parent = null;

	@Column(name = "parent_id", insertable = false, updatable = false)
	private Long parentId;

	public String getThumbnail() {
		if (wistiaVideoData != null) {
			return wistiaVideoData.getThumbnail();
		} else {
			return youtubeVideoData.getVideoThumnail();
		}
	}

	public VideoData getVideoData() {
		if (wistiaVideoData != null) {
			return new VideoData(Provider.WISTIA, wistiaVideoData.getVideoId());
		} else {
			return new VideoData(Provider.YOUTUBE,
					youtubeVideoData.getVideoId());
		}
	}

	public ContestAd getContestAd() {
		return contestAd;
	}

	public void setContestAd(ContestAd contestAd) {
		this.contestAd = contestAd;
	}

	public String getVideoUrl() {
		if (wistiaVideoData != null) {
			return wistiaVideoData.getVideoUrl();
		} else {
			return youtubeVideoData.getVideoUrl();
		}
	}

	public Double getRank() {
		if (rank == null)
			return new Double(0);
		return rank;
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

	// public void addRank(Rank rank) {
	// rank.setAd(this);
	// if (ranks == null) {
	// ranks = new ArrayList<Rank>();
	// }
	// ranks.add(rank);
	// }

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

	public YoutubeVideoData getYoutubeVideoData() {
		return youtubeVideoData;
	}

	public void setYoutubeVideoData(YoutubeVideoData youtubeVideoData) {
		this.youtubeVideoData = youtubeVideoData;
	}

	public Boolean getOfficial() {
		return official;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Ad getParent() {
		return parent;
	}

	public void setParent(Ad parent) {
		this.parent = parent;
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

	public WistiaVideoData getWistiaVideoData() {
		return wistiaVideoData;
	}

	public void setWistiaVideoData(WistiaVideoData wistiaVideoData) {
		this.wistiaVideoData = wistiaVideoData;
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
