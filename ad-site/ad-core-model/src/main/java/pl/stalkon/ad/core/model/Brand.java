package pl.stalkon.ad.core.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name = "brands")
public class Brand extends CommonEntity {
	private static final long serialVersionUID = -3646540035738604078L;

	@Column(length = 1024, nullable = false)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "brand")
	private List<Ad> ads;

	@Column(nullable = false)
	private String name;

	@Column(name = "logo_url")
	private String logoUrl;

	@Column(name = "small_logo_url")
	private String smallLogoUrl;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "wistia_project_data_id")
	private WistiaProjectData wistiaProjectData;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	// @JoinTable(name = "company_brand", inverseJoinColumns = {
	// @JoinColumn(updatable = false, name = "company_id", referencedColumnName
	// = "id") }, joinColumns = { @JoinColumn(updatable = false, name =
	// "brand_id", referencedColumnName = "id") })
	@JoinColumn(name = "company_id")
	private Company company;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name = "creation_date")
	private Date creationDate;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "brand_cost_map", inverseJoinColumns = { @JoinColumn(updatable = false, name = "cost_id", referencedColumnName = "id") }, joinColumns = { @JoinColumn(updatable = false, name = "brand_id", referencedColumnName = "id") })
	private List<Cost> costs;

	public List<Ad> getAds() {
		return ads;
	}

	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}

	// public void addAdd(Ad ad) {
	// ads.add(ad);
	// ad.setBrand(this);
	// }

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

	public WistiaProjectData getWistiaProjectData() {
		return wistiaProjectData;
	}

	public void setWistiaProjectData(WistiaProjectData wistiaProjectData) {
		this.wistiaProjectData = wistiaProjectData;
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

	public List<Cost> getCosts() {
		return costs;
	}

	public void setCosts(List<Cost> costs) {
		this.costs = costs;
	}

}
