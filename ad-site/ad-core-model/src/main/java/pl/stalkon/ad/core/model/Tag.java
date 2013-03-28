package pl.stalkon.ad.core.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="tag")
public class Tag extends CommonEntity {

	@NotNull
	private String name;
	private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
