package pl.stalkon.video.api.service.impl;

import java.awt.Label;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.WistiaVideoData;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.video.api.wistia.WistiaStats;

@Component
public class WistiaApiService implements InitializingBean {

	private RestTemplate restTemplate;
	private MultiValueMap<String, String> headers;

	@Autowired
	private Environment env;

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
		WistiaVideoData wistiaVideoData = new WistiaVideoData(
				adPostDto.getVideoId(), adPostDto.getThumbnail(),
				adPostDto.getDuration());
		ad.setWistiaVideoData(wistiaVideoData);
		return ad;
	}

	public void setApiData(Ad ad) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", ad.getTitle());
		map.put("description", ad.getDescription());
		Map<String, String> vars = Collections.singletonMap("videoId", ad
				.getWistiaVideoData().getVideoId());
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
		Map<String, String> vars = Collections.singletonMap("projectId",
				hashedId);
		Map<String, String> map = new HashMap<String, String>();
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		map.put("start_date", sm.format(startDate));
		map.put("end_date", sm.format(endDate));
		ResponseEntity<Map> response = restTemplate.exchange(env
				.getProperty("wistia.statsProjectJsonUrl.by.date"),
				HttpMethod.GET, new HttpEntity<Map<String, String>>(map,
						headers), Map.class, vars);
		Date date = null;
		try {
			date = sm.parse((String) response.getBody().get("date"));
		} catch (ParseException e) {
			// ignore;
		}
		return new WistiaStats(date, new Long((String) response.getBody().get(
				"load_count")), new Long((String) response.getBody().get(
				"play_count")), new Double((String) response.getBody().get(
				"hours_watched")));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configureRestTemplate();

	}
}
