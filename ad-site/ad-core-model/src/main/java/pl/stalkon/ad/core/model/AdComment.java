package pl.stalkon.ad.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="ad_comments")
public class AdComment extends CommonEntity {

	private static final long serialVersionUID = 6442354847488801511L;

	@ManyToOne(fetch = FetchType.EAGER, optional=false)
	@JoinColumn(name="user_id")
	private User user;

	@NotNull
	@Column(length = 512, nullable=false)
	private String message;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", name="creation_date")
	private Date creationDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="parent", cascade=CascadeType.ALL)
	@OrderBy(value="creation_date")
	private List<AdComment> children;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = true, updatable = false)
	private AdComment parent = null;

	@ManyToOne(fetch = FetchType.LAZY, optional=true)
	private Ad ad;

	public Ad getAd() {
		return ad;
	}

	public void setAd(Ad ad) {
		this.ad = ad;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date date) {
		this.creationDate = date;
	}

	public List<AdComment> getChildren() {
		return children;
	}

	public void setChildren(List<AdComment> children) {
		this.children = children;
	}

	public AdComment getParent() {
		return parent;
	}

	public void setParent(AdComment parent) {
		this.parent = parent;
	}

}
