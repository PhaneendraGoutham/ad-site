package pl.stalkon.ad.core.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import pl.styall.library.core.ext.validation.FieldMatch;

@FieldMatch.List({ @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"), })
public class ChangePasswordDto {
	@NotNull
	private String oldPassword;

	@NotNull
	@Size(min = 6, max = 20)
	@Pattern(regexp = "^[0-9a-zA-Z,.-=;'!@#$%^&*()_,.]+$")
	private String password;
	@NotNull
	private String confirmPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
