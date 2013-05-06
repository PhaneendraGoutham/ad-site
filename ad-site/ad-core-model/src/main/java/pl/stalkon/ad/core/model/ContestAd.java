package pl.stalkon.ad.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="contest_ads")
public class ContestAd extends CommonEntity {

	private static final long serialVersionUID = -5623514456875384392L;
	

	
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="ad_id")
	private Ad ad;
	
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	@JoinColumn(name="contest_id")
	private Contest contest;
	
	@Column(nullable=false)
	private Boolean winner = false;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name="creation_date")
	private Date creationDate;
	public Boolean getWinner() {
		return winner;
	}

	public void setWinner(Boolean winner) {
		this.winner = winner;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public Contest getContest() {
		return contest;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	


}
