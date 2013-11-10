package pl.stalkon.dailymotion.api.module.service.impl;


import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.dmcloud.cloudkey.CloudKey;
import net.dmcloud.cloudkey.DCException;
import net.dmcloud.cloudkey.Helpers;
import net.dmcloud.util.DCArray;
import net.dmcloud.util.DCObject;
import pl.stalkon.ad.core.model.DailymotionData;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;
import pl.stalkon.dailymotion.api.module.service.UploadStatus;

@Service("dailyMotionService")
public class DailymotionServiceImpl implements DailymotionService {

	@Autowired
	private Environment env;
	
	private Logger log =  Logger.getLogger(DailymotionServiceImpl.class);

	@Override
	public UploadStatus getStatus(String callback) throws DailymotionException {
		CloudKey cloudKey = new CloudKey(env.getProperty("dailymotion.userId"), env.getProperty("dailymotion.apiKey"));
		DCObject result;
		try {
			result = cloudKey.fileUpload(true, "?", callback);
			UploadStatus uploadStatus = new UploadStatus(result.pull("url"),
					result.pull("status"));
			return uploadStatus;
		} catch (Exception e) {
			throw new DailymotionException(e.getMessage());
		}

	}

	@Override
	public UploadStatus getStatus() throws DailymotionException {
		return getStatus("");
	}

	@Override
	public DailymotionData createMedia(String url, String title)
			throws DailymotionException {
		CloudKey cloudKey = new CloudKey(env.getProperty("dailymotion.userId"), env.getProperty("dailymotion.apiKey"));
		DCArray assets = DCArray.create().push("mp4_h264_aac_hq")
				.push("mp4_h264_aac_hd").push("jpeg_thumbnail_medium")
				.push("jpeg_thumbnail_large");
		DCObject metaObject = DCObject.create()
				.push("title",title);

		String id;
		
		try {
			id = cloudKey.mediaCreate(url, assets, metaObject);
			
		} catch (Exception e) {
			throw new DailymotionException(e.getMessage());
		}
		return new DailymotionData(id, getEmbeddedUrl(id), "");
	}

	private String getEmbeddedUrl(String id) throws DailymotionException {
		String url = env.getProperty("dailymotion.embeddedUrl") + env.getProperty("dailymotion.userId") +"/" + id;
		try {
			int expires = (int)(new Date().getTime()/1000) + new Integer(env.getProperty("dailymotion.videoExpires"));
			String signedUrl = Helpers.sign_url(url, env.getProperty("dailymotion.apiKey"), CloudKey.SECLEVEL_NONE, "", "", "", null,null, expires);
			return signedUrl;
		} catch (DCException e) {
			throw new DailymotionException(e.getMessage());
		}
	}

	@Override
	public String getMediumThumbnailUrl(String id)throws DailymotionException {
		DCObject result;
		try {
			CloudKey cloudKey = new CloudKey(env.getProperty("dailymotion.userId"), env.getProperty("dailymotion.apiKey"));
			result = cloudKey.call(
		                "media.get_assets",
		                DCObject.create()
	                    .push("id", id)
	                    .push("assets_names", DCArray.create().push("jpeg_thumbnail_medium"))
		                );
		} catch (Exception e) {
			throw new DailymotionException(e.getMessage());
		}
		Map info =(Map) result.get("jpeg_thumbnail_medium");
		return null;
	}
	
	
	

}
