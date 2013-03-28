package pl.stalkon.dailymotion.api.module.service;

import pl.stalkon.ad.core.model.DailymotionData;

public interface DailymotionService {
	public UploadStatus getStatus(String callback) throws DailymotionException;
	public UploadStatus getStatus() throws DailymotionException;
	public DailymotionData createMedia(String url, String title) throws DailymotionException;
	public String getMediumThumbnailUrl(String id)throws DailymotionException;
}
