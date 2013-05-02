package pl.stalkon.ad.core.model.dto;

public class BrandSearchDto extends AutocompleteDto{
	private String projectId;
	
	
	public BrandSearchDto(String label, Long value, String projectId) {
		super(label, value);
		this.projectId = projectId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
}
