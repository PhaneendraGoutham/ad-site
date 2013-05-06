package pl.stalkon.ad.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="wistia_projects")
public class WistiaProjectData extends CommonEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7025572099680238801L;
	
	@Column(name="wistia_id", nullable=false)
	private Long wistiaId;
	
	@Column(name="hashed_id", nullable=false)
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
