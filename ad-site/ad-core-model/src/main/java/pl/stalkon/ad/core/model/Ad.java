package pl.stalkon.ad.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.defaultimpl.User;

@Entity
@Table(name="ad")
public class Ad implements CommonEntity {

	private static final long serialVersionUID = 1335218634734582331L;
	
	public enum Type {MOVIE, PICTURE, GAME}
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@URL
	private String link;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private User poster;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private AdData adData;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Brand brand;
	
	private Type type;
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public User getPoster() {
		return poster;
	}


	public void setPoster(User poster) {
		this.poster = poster;
	}


	private void setId(Long id) {
		this.id = id;
	}


	public Long getId() {
		return id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public AdData getAdData() {
		return adData;
	}

	public void setAdData(AdData adData) {
		this.adData = adData;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
