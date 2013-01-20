package pl.stalkon.ad.core.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="adData")
public class AdData implements CommonEntity {

	private static final long serialVersionUID = -6411065199654674571L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	private String description;

	private String title;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
