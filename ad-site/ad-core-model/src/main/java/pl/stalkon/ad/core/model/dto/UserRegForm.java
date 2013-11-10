package pl.stalkon.ad.core.model.dto;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import pl.stalkon.ad.core.model.User.DisplayNameType;
import pl.styall.library.core.form.UserFormModel;
import pl.styall.library.core.model.AbstractUserData.Sex;

public class UserRegForm extends UserFormModel {

	private static final long serialVersionUID = -2086236119630399425L;

	@NotNull
	@AssertTrue
	private boolean terms;

	@NotNull
	@DateTimeFormat(pattern = "YYYY-MM-DD")
	private Date birthdate;

	@NotNull
	private Sex sex;

	private DisplayNameType displayNameType;

	@Size(min = 3, max = 20)
	@Pattern(regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ-]+$")
	private String name;

	@Size(min = 3, max = 20)
	@Pattern(regexp = "^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ-]+$")
	private String surname;
	
	private String captcha;

//	private CommonsMultipartFile avatarFile;

//	public CommonsMultipartFile getAvatarFile() {
//		return avatarFile;
//	}
//
//	public void setAvatarFile(CommonsMultipartFile avatarFile) {
//		this.avatarFile = avatarFile;
//	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
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

	public DisplayNameType getDisplayNameType() {
		return displayNameType;
	}

	public void setDisplayNameType(DisplayNameType displayNameType) {
		this.displayNameType = displayNameType;
	}

	public boolean isTerms() {
		return terms;
	}

	public void setTerms(boolean terms) {
		this.terms = terms;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

}
