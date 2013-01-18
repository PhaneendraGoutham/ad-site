package pl.stalkon.ad.core.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.styall.library.core.model.CommonEntity;

public class AdData implements CommonEntity {

	private static final long serialVersionUID = -6411065199654674571L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private String desc;

	private String title;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Brand brand;

	@Override
	public Long getId() {
		return id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

}
