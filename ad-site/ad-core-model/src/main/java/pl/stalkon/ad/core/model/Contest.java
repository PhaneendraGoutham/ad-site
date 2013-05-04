package pl.stalkon.ad.core.model;

import java.util.ArrayList;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "contest")
public class Contest extends CommonEntity {

	private static final long serialVersionUID = 116695731544765566L;

	public enum State {
		FINISHED, ON_GOING
	};

	public enum Type {
		AD, ANSWER_QUESTION
	}

	private String name;
	private String description;
	private String imageUrl;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationDate;

	private Date finishDate;
	private Date scoresDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Brand brand;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

//	@OneToMany(fetch = FetchType.LAZY, mappedBy="contest")
//	private List<Ad> ads;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="contest", cascade=CascadeType.PERSIST)
	private List<ContestAd> contestAds;

	private State state;
	
	private Boolean active;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="contest", cascade=CascadeType.ALL)
	private List<ContestAnswer> answers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		byte decoded[] = Base64.decodeBase64(description.getBytes());
		return new String(decoded);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

//	public List<Ad> getAds() {
//		return ads;
//	}
//
//	public void setAds(List<Ad> ads) {
//		this.ads = ads;
//	}
//
//	public void addAd(Ad ad) {
//		if (ads == null) {
//			ads = new ArrayList<Ad>();
//		}
//		ads.add(ad);
//	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	private Type type;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<ContestAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<ContestAnswer> answers) {
		this.answers = answers;
	}

	public List<ContestAd> getContestAds() {
		return contestAds;
	}

	public void setContestAds(List<ContestAd> contestAds) {
		this.contestAds = contestAds;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
