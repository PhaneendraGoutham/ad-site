package pl.stalkon.ad.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "ranks", uniqueConstraints={@UniqueConstraint(columnNames={"user_id", "ad_id"})})
public class Rank extends CommonEntity {

	private static final long serialVersionUID = -7464103449661778198L;

	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="ad_id")
	private Ad ad;
	
	@Column(nullable=false)
	private Short rank;

	public Short getRank() {
		return rank;
	}

	public void setRank(Short rank) {
		this.rank = rank;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}
	

}
