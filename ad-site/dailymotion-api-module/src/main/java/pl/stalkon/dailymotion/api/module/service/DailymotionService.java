package pl.stalkon.dailymotion.api.module.service;

import java.util.Map;


public interface DailymotionService {
	public UploadStatus getStatus(String callback);
	public UploadStatus getStatus();
	public String createMedia(String url, Map<String, Object> meta);
}
