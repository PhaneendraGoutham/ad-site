package pl.stalkon.dailymotion.api.module.service.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import net.dmcloud.cloudkey.CloudKey;
import net.dmcloud.util.DCArray;
import net.dmcloud.util.DCObject;
import pl.stalkon.dailymotion.api.module.service.DailymotionService;
import pl.stalkon.dailymotion.api.module.service.UploadStatus;

@Service("dailyMotionService")
public class DailymotionServiceImpl implements DailymotionService {

	private static final String apiKey = "d09f4a20348b3b5a229fdada9cb183e0e0a0e38e";
	private static final String userId = "510fac5794a6f6702601b20b";

	private static Logger log = Logger.getLogger(DailymotionServiceImpl.class);

	@Override
	public UploadStatus getStatus(String callback) {
		CloudKey cloudKey = new CloudKey(userId, apiKey);
		try {
			DCObject result = cloudKey.fileUpload(true, "?", callback);
			UploadStatus uploadStatus = new UploadStatus(result.pull("url"),
					result.pull("status"));
			return uploadStatus;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public UploadStatus getStatus() {
		return getStatus("");
	}

	@Override
	public String createMedia(String url, Map<String, Object> meta) {
		CloudKey cloudKey = new CloudKey(userId, apiKey);
		DCArray assets = DCArray.create().push("mp4_h264_aac_ld")
				.push("mp4_h264_aac_hd").push("jpeg_thumbnail_medium")
				.push("jpeg_thumbnail_large");
		DCObject metaObject = DCObject.create()
				.push("title", meta.get("title"))
				.push("description", meta.get("description"));
		try {
			String id = cloudKey.mediaCreate(url, assets, metaObject);
			return id;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

}
