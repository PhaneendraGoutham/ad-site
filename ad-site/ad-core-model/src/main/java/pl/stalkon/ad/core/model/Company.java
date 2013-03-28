package pl.stalkon.ad.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pl.styall.library.core.model.AbstractCompany;
import pl.styall.library.core.model.CommonEntity;
import pl.styall.library.core.model.defaultimpl.Address;

@Entity
@Table(name="company")
public class Company extends AbstractCompany<Address>{

	private static final long serialVersionUID = -3276193432406949362L;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
}
