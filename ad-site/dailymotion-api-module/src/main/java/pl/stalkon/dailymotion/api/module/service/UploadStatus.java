package pl.stalkon.dailymotion.api.module.service;

public class UploadStatus {
	
	private final String url;
	private final String status;
	
	public UploadStatus(String url, String status) {
		super();
		this.url = url;
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public String getStatus() {
		return status;
	}
	
	
}
