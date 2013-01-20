package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.defaultimpl.Company;

@Entity
@Table(name="brand")
public class Brand implements CommonEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3646540035738604078L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Brand parent;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Company company;
	
	private String description;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="brand")
	private List<Ad> ads = new ArrayList<Ad>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="parent")
	private List<Brand> children;
	

	@Override
	public Long getId() {
		return id;
	}


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


	public void setId(Long id) {
		this.id = id;
	}


	public List<Brand> getChildren() {
		return children;
	}
	public void addChild(Brand brand){
		this.children.add(brand);
		brand.setParent(this);
	}


	public void setChildren(List<Brand> children) {
		this.children = children;
	}

}
