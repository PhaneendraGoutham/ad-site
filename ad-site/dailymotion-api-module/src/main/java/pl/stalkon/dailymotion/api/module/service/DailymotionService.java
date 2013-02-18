package pl.stalkon.dailymotion.api.module.service;

import java.util.Map;

import net.dmcloud.cloudkey.DCException;


public interface DailymotionService {
	public UploadStatus getStatus(String callback) throws DailymotionException;
	public UploadStatus getStatus() throws DailymotionException;
	public String createMedia(String url, Map<String, Object> meta) throws DailymotionException;
	public String getEmbeddedUrl(String id) throws DailymotionException;
}
