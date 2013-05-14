package pl.stalkon.ad.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.styall.library.core.model.CommonEntity;


@Entity
@Table(name="costs")
public class Cost extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7940651754069765734L;
	
	@Column(nullable=false, name="const_per_minute_per_1000")
	private Double costPerSecondPer1000;
	


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_from", nullable=false)
	private Date dateFrom;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_to")
	private Date dateTo;

	
	@Column(nullable=false)
	private Boolean current;

	public Double getCostPerSecondPer1000() {
		return costPerSecondPer1000;
	}


	public void setCostPerSecondPer1000(Double costPerSecondPer1000) {
		this.costPerSecondPer1000 = costPerSecondPer1000;
	}



	public Date getDateFrom() {
		return dateFrom;
	}


	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}


	public Date getDateTo() {
		return dateTo;
	}


	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}


	public Boolean getCurrent() {
		return current;
	}


	public void setCurrent(Boolean current) {
		this.current = current;
	}

}
