package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.defaultimpl.Company;

public class Brand implements CommonEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3646540035738604078L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Brand parent;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private Company company;
	
	private String desc;
	
	private List<Ad> ads = new ArrayList<Ad>();
	

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


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public List<Ad> getAds() {
		return ads;
	}


	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
	public void addAdd(Ad ad) {
		ads.add(ad);
	}


	public void setId(Long id) {
		this.id = id;
	}

}
