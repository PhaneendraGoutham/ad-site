package pl.stalkon.ad.core.model.dto;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import pl.styall.library.core.model.defaultimpl.Address;

public class UserAddressDto {

	@NotEmpty
	private String firstname;
	@NotEmpty
	private String surname;
	@Valid
	private Address address;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
