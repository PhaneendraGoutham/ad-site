package pl.stalkon.ad.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import pl.styall.library.core.model.AbstractCompany;
import pl.styall.library.core.model.defaultimpl.Address;

@Entity
@Table(name = "companies")
public class Company extends AbstractCompany<Address> {

	private static final long serialVersionUID = -3276193432406949362L;

	
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="company")
	private List<Brand> brands;
	
	@Column(nullable=false)
	private boolean approved;
	

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="user_id")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}


}
