package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "brand")
public class Brand extends CommonEntity {
	private static final long serialVersionUID = -3646540035738604078L;

	@Column(length=1024, nullable=false)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "brand")
	private List<Ad> ads = new ArrayList<Ad>();

	@Column(nullable=false)
	private String name;
	
	private String logoUrl;
	private String smallLogoUrl;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "wistiaProject")
	private WistiaProject wistiaProject;

	
	@ManyToOne(fetch=FetchType.LAZY, optional=true)
	@JoinTable(name = "company_brand", inverseJoinColumns = { @JoinColumn(updatable = false, name = "company_id", referencedColumnName = "id") },
	joinColumns = { @JoinColumn(updatable = false, name = "brand_id", referencedColumnName = "id") })
	private Company company;

	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date creationDate;
	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	public void addAdd(Ad ad) {
		ads.add(ad);
		ad.setBrand(this);
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrlo(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WistiaProject getWistiaProject() {
		return wistiaProject;
	}

	public void setWistiaProject(WistiaProject wistiaProject) {
		this.wistiaProject = wistiaProject;
	}


	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public String getSmallLogoUrl() {
		return smallLogoUrl;
	}

	public void setSmallLogoUrl(String smallLogoUrl) {
		this.smallLogoUrl = smallLogoUrl;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
