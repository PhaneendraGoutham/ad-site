package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.defaultimpl.Company;

@Entity
@Table(name = "brand", uniqueConstraints={@UniqueConstraint(columnNames="name")})
public class Brand extends CommonEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3646540035738604078L;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Brand parent;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Company company;
	
	@NotNull
	@Size(max=512)
	@Column(length=512, nullable=false)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "brand")
	private List<Ad> ads = new ArrayList<Ad>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parent")
	private List<Brand> children;

	@NotNull
	@Size(max=255, min=3)
	private String name;
	
	private String logo;
	
	public Brand getParent() {
		return parent;
	}

	public void setParent(Brand parent) {
		this.parent = parent;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

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

	public List<Brand> getChildren() {
		return children;
	}

	public void addChild(Brand brand) {
		this.children.add(brand);
		brand.setParent(this);
	}

	public void setChildren(List<Brand> children) {
		this.children = children;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
