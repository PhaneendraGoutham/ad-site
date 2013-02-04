package pl.stalkon.ad.core.model;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

//@Embeddable
public class UserAdPK {

	private static final long serialVersionUID = -2912693560354598053L;
	  
	@ManyToOne(fetch=FetchType.LAZY)
	  private Long userId;

	  
	  @ManyToOne(fetch=FetchType.LAZY)
	  private Long adId;

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
	    result = prime * result + ((adId == null) ? 0 : adId.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null) return false;
	    if (getClass() != obj.getClass()) return false;
	    final UserAdPK other = (UserAdPK) obj;
	    if (userId == null) {
	      if (other.userId != null) return false;
	    } else if (!userId.equals(other.userId)) return false;
	    if (adId == null) {
	      if (other.adId != null) return false;
	    } else if (!adId.equals(other.adId)) return false;
	    return true;
	  }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

}
