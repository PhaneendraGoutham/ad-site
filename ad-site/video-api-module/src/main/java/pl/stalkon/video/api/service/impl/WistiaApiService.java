package pl.stalkon.video.api.service.impl;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.VideoData;
import pl.stalkon.ad.core.model.VideoData.Provider;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.video.api.service.WistiaFetchVideoDataScheduler;
import pl.stalkon.video.api.wistia.WistiaStats;

@Component
public class WistiaApiService implements InitializingBean {

	private RestTemplate restTemplate;
	private MultiValueMap<String, String> headers;

	@Autowired
	private Environment env;
	
	@Autowired
	private WistiaFetchVideoDataScheduler fetchDataScheduler;

	private void configureRestTemplate() {
		restTemplate = new RestTemplate(
				new HttpComponentsClientHttpRequestFactory());
		headers = getAuthHeaders();
	}

	private MultiValueMap<String, String> getAuthHeaders() {
		MultiValueMap<String, String> headers = new org.springframework.http.HttpHeaders();
		String auth = env.getProperty("wistia.username") + ":"
				+ env.getProperty("wistia.password");
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset
				.forName("UTF-8")));
		String authHeader = "Basic " + new String(encodedAuth);
		headers.add("Authorization", authHeader);
		return headers;
	}

	public Ad setVideoDetails(AdPostDto adPostDto) {
		Ad ad = new Ad();
		ad.setApproved(false);
		VideoData wistiaVideoData = new VideoData(adPostDto.getVideoId(), Provider.WISTIA);
		ad.setVideoData(wistiaVideoData);
		fetchDataScheduler.scheduleWistiaFetchVideoData(adPostDto.getVideoId());
		return ad;
	}

	public VideoData getVideoEmbeddedData(Ad ad){
		Map<String, Object> data = getVideoAssets(ad);
		VideoData wistiaVideoData = processVideoAssets(data,ad.getVideoData().getVideoId());
		return wistiaVideoData;
	}

	private VideoData processVideoAssets(
			Map<String, Object> videoAssets, String videoId) {
		if(!videoAssets.get("status").equals("ready"))
			return null;
		Map<String, Object> thumbnail = (Map<String, Object>) videoAssets
				.get("thumbnail");
		List<Map<String, Object>> assests = (List<Map<String, Object>>) videoAssets
				.get("assets");
		Long maxSize = new Long(0);
		String videoUrl = null;
		for (Map asset : assests) {
			if (((String)asset.get("type")).equalsIgnoreCase("OriginalFile")
					|| ((String)asset.get("type")).equalsIgnoreCase("StillImageFile") || ((String)asset.get("type")).equalsIgnoreCase("IphoneVideoFile"))
				continue;
			Long temp = new Long((Integer) asset.get("fileSize"));
			if (temp > maxSize) {
				maxSize = temp;
				videoUrl = (String) asset.get("url");
				System.out.println(asset.get("type"));
			}
		}
		return new VideoData(videoId, ((String) thumbnail.get("url")).replaceAll("\\?image_crop_resized=.*&?", ""),(Double)videoAssets.get("duration"),videoUrl, Provider.WISTIA);
	}

	private Map<String, Object> getVideoAssets(Ad ad) {
		String videoId = ad.getVideoData().getVideoId();
		Map<String, String> vars = Collections.singletonMap("videoId", videoId);
		ResponseEntity<Map> response = restTemplate.exchange(
				env.getProperty("wistia.mediaShowJsonUrl"), HttpMethod.GET,
				new HttpEntity<Map<String, String>>(null, headers), Map.class,
				vars);
		return (Map<String, Object>) response.getBody();
	}

	public void setApiData(Ad ad) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", ad.getTitle());
		map.put("description", ad.getDescription());
		Map<String, String> vars = Collections.singletonMap("videoId", ad
				.getVideoData().getVideoId());
		restTemplate.exchange(env.getProperty("wistia.mediaJsonUrl"),
				HttpMethod.PUT, new HttpEntity<Map<String, String>>(map,
						headers), Map.class, vars);
	}

	public WistiaProjectData createWistiaProject(String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("anonymousCanUpload", "1");
		map.put("anonymousCanDownload", "0");
		ResponseEntity<Map> response = restTemplate.exchange(
				env.getProperty("wistia.createProjectUrl"), HttpMethod.POST,
				new HttpEntity<Map<String, String>>(map, headers), Map.class);
		Map<String, Object> result = response.getBody();
		WistiaProjectData wistiaProjectData = new WistiaProjectData();
		wistiaProjectData.setWistiaId(new Long((Integer) result.get("id")));
		wistiaProjectData.setHashedId((String) result.get("hashedId"));
		return wistiaProjectData;
	}

	public boolean deleteVideo(String hashedId) {
		Map<String, String> vars = Collections
				.singletonMap("videoId", hashedId);
		ResponseEntity<Map> response = restTemplate.exchange(
				env.getProperty("wistia.mediaJsonUrl"), HttpMethod.DELETE,
				new HttpEntity<Map<String, String>>(null, headers), Map.class,
				vars);
		return response.getStatusCode().equals(HttpStatus.OK);
	}

	public boolean deleteProject(String hashedId) {
		Map<String, String> vars = Collections.singletonMap("projectId",
				hashedId);
		ResponseEntity<Map> response = restTemplate.exchange(env
				.getProperty("wistia.mediaProjectJsonUrl"), HttpMethod.DELETE,
				new HttpEntity<Map<String, String>>(null, headers), Map.class,
				vars);
		return response.getStatusCode().equals(HttpStatus.OK);
	}

	public WistiaStats getProjectStats(String hashedId, Date startDate,
			Date endDate) {
		// Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, String> vars = new HashMap<String, String>(3);
		vars.put("projectId", hashedId);
		vars.put("start_date", sm.format(startDate));
		vars.put("end_date", sm.format(endDate));

		// map.put("start_date", sm.format(startDate));
		// map.put("end_date", sm.format(endDate));
		ResponseEntity<List> response = restTemplate.exchange(
				env.getProperty("wistia.statsProjectJsonUrl.by.date")
						+ "?start_date={start_date}&end_date={end_date}",
				HttpMethod.GET, new HttpEntity<Map<String, String>>(null,
						headers), List.class, vars);
		Long loadCount = new Long(0);
		Long playCount = new Long(0);
		Double hoursWatched = new Double(0);
		for (int i = 0; i < response.getBody().size(); i++) {
			Map<String, Object> stats = ((Map<String, Object>) response
					.getBody().get(i));
			loadCount += new Long((Integer) stats.get("load_count"));
			playCount += new Long((Integer) stats.get("play_count"));
			if (stats.get("hours_watched") instanceof Integer) {
				hoursWatched += new Double((Integer) stats.get("hours_watched"));
			} else {
				hoursWatched += (Double) stats.get("hours_watched");
			}
		}
		return new WistiaStats(loadCount, playCount, hoursWatched);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configureRestTemplate();

	}
}
