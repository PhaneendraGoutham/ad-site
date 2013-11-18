package pl.stalkon.ad.core.model;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import pl.styall.library.core.model.CommonEntity;

@Entity
@Table(name="tags")
public class Tag extends CommonEntity {

	public static final List<String> JSON_SM_SHOW = Arrays.asList("id", "name");
	/**
	 * 
	 */
	private static final long serialVersionUID = -1036182504585898010L;
	
	
	@Column(length=64, nullable=false)
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
