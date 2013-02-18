package pl.stalkon.dailymotion.api.module.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.dmcloud.cloudkey.CloudKey;
import net.dmcloud.cloudkey.DCException;
import net.dmcloud.cloudkey.Helpers;
import net.dmcloud.util.DCArray;
import net.dmcloud.util.DCObject;
import pl.stalkon.dailymotion.api.module.service.DailymotionException;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;
import pl.stalkon.dailymotion.api.module.service.UploadStatus;

@Service("dailyMotionService")
public class DailymotionServiceImpl implements DailymotionService {

	private static final String apiKey = "d09f4a20348b3b5a229fdada9cb183e0e0a0e38e";
	private static final String userId = "510fac5794a6f6702601b20b";
	private static final String URL = "http://api.dmcloud.net/player/embed/" + userId + "/";
	private static Logger log = Logger.getLogger(DailymotionServiceImpl.class);

	@Override
	public UploadStatus getStatus(String callback) throws DailymotionException {
		CloudKey cloudKey = new CloudKey(userId, apiKey);
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
	public String createMedia(String url, Map<String, Object> meta)
			throws DailymotionException {
		CloudKey cloudKey = new CloudKey(userId, apiKey);
		DCArray assets = DCArray.create().push("mp4_h264_aac_hq")
				.push("mp4_h264_aac_hd").push("jpeg_thumbnail_medium")
				.push("jpeg_thumbnail_large");
		DCObject metaObject = DCObject.create()
				.push("title", meta.get("title"))
				.push("description", meta.get("description"));
		String id;
		try {
			id = cloudKey.mediaCreate(url, assets, metaObject);
		} catch (Exception e) {
			throw new DailymotionException(e.getMessage());
		}
		return id;
	}

	@Override
	public String getEmbeddedUrl(String id) throws DailymotionException {
		String url = URL + id;
		try {
			String signedUrl = Helpers.sign_url(url, apiKey, CloudKey.SECLEVEL_NONE, "", "", "", null, null, 0);
			return signedUrl;
		} catch (DCException e) {
			throw new DailymotionException(e.getMessage());
		}
	}

}
