package pl.stalkon.ad.core.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="wistia_project")
public class WistiaProject extends CommonEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7025572099680238801L;
	private Long wistiaId;
	private String hashedId;
	public Long getWistiaId() {
		return wistiaId;
	}
	public void setWistiaId(Long wistiaId) {
		this.wistiaId = wistiaId;
	}
	public String getHashedId() {
		return hashedId;
	}
	public void setHashedId(String hashedId) {
		this.hashedId = hashedId;
	}
}
