package pl.stalkon.ad.core.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pl.styall.library.core.model.AbstractCompany;
import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.UserRole;
import pl.styall.library.core.model.defaultimpl.Address;

@Entity
@Table(name = "company")
public class Company extends AbstractCompany<Address> {

	private static final long serialVersionUID = -3276193432406949362L;

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="company")
	private List<Brand> brands;

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
