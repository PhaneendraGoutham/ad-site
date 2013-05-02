package pl.stalkon.ad.core.model.dto;

public class UserProfileDto {
	private String name;
	private String surname;
	private String email;
	private String username;

	public UserProfileDto(String name, String surname, String email,
			String username) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.username = username;
	}
	
	public UserProfileDto(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
