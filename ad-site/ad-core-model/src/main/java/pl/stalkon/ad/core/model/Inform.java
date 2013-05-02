package pl.stalkon.ad.core.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="inform")
public class Inform extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6294062458046700422L;
	private String message;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	private Ad ad;
	
	@ManyToOne(fetch=FetchType.EAGER,optional=true)
	private AdComment adComment;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}

	public AdComment getAdComment() {
		return adComment;
	}

	public void setAdComment(AdComment adComment) {
		this.adComment = adComment;
	}
}
