package pl.stalkon.ad.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="contest_ad")
public class ContestAd extends CommonEntity {

	private static final long serialVersionUID = -5623514456875384392L;
	

	
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="ad_id")
	private Ad ad;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="contest_id")
	private Contest contest;
	
	@Column(nullable=false)
	private Boolean winner = false;
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
	
	


}
