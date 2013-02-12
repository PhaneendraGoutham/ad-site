package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Loader;
import org.hibernate.validator.constraints.URL;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "ad")
//@NamedNativeQuery(name = "rankCount", query = "SELECT COUNT(rank) from rank where adId = ?")
public class Ad extends CommonEntity {

	private static final long serialVersionUID = 1335218634734582331L;

	public enum Type {
		MOVIE, PICTURE, GAME
	}

	@NotNull
	private String dailymotionId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User poster;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private AdData adData;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Brand brand;

	private Type type;

	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="ad")
	private List<Rank> ranks;
	
	@Formula("(SELECT SUM(r.rank) from ranks as r where r.adId = id)")
//	@Transient
	private Long rank;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY,mappedBy="ad")
	private Set<AdComment> comments = new HashSet<AdComment>();

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public String getDailymotionId() {
		return dailymotionId;
	}

	public void setDailymotionId(String dailymotionId) {
		this.dailymotionId = dailymotionId;
	}

	public User getPoster() {
		return poster;
	}

	public void setPoster(User poster) {
		this.poster = poster;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public AdData getAdData() {
		return adData;
	}

	public void setAdData(AdData adData) {
		this.adData = adData;
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
		if(ranks == null){
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


}
