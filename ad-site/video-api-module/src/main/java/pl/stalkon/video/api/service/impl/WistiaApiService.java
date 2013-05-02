package pl.stalkon.video.api.service.impl;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.WistiaProject;
import pl.stalkon.ad.core.model.WistiaVideo;
import pl.stalkon.ad.core.model.dto.AdPostDto;

@Component
public class WistiaApiService implements InitializingBean {

	private RestTemplate restTemplate;
	private MultiValueMap<String, String> headers;

	@Autowired
	private Environment env;

	private void configureRestTemplate() {
		// DefaultHttpClient client = new DefaultHttpClient();
		// BasicCredentialsProvider credentialsProvider = new
		// BasicCredentialsProvider();
		// String username = env.getProperty("wistia.username");
		// credentialsProvider.setCredentials(AuthScope.ANY, new
		// UsernamePasswordCredentials(username,
		// env.getProperty("wistia.password")));
		// client.setCredentialsProvider(credentialsProvider);
		// ClientHttpRequestFactory rf = new
		// HttpComponentsClientHttpRequestFactory(client);
		// HttpHost wistiaHost = new HttpHost(env.getProperty("wistia.host"));
		// HttpComponentsClientHttpRequestFactoryBasicAuth requestFactory = new
		// HttpComponentsClientHttpRequestFactoryBasicAuth(
		// wistiaHost);
		// restTemplate = new RestTemplate();
		// restTemplate.setRequestFactory(requestFactory);
		// DefaultHttpClient httpClient = (DefaultHttpClient) requestFactory
		// .getHttpClient();
		// httpClient.getCredentialsProvider().setCredentials(
		// new AuthScope(wistiaHost,AuthScope.ANY_REALM,"https"),
		// new UsernamePasswordCredentials(env.getProperty("wistia.username"),
		// env.getProperty("wistia.password")));
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
		WistiaVideo wistiaVideo = new WistiaVideo(adPostDto.getVideoId(),
				adPostDto.getThumbnail());
		ad.setWistiaVideo(wistiaVideo);
		return ad;
	}

	public void setApiData(Ad ad) {
		Map<String, String> map = Collections.singletonMap("name",
				ad.getTitle());
		Map<String, String> vars = Collections.singletonMap("videoId", ad
				.getWistiaVideo().getVideoId());
		restTemplate.exchange(env.getProperty("wistia.mediaJsonUrl"),
				HttpMethod.PUT, new HttpEntity<Map<String, String>>(map,
						headers), Map.class, vars);
	}

	public WistiaProject createWistiaProject(String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", name);
		map.put("anonymousCanUpload", "1");
		map.put("anonymousCanDownload", "0");
		ResponseEntity<Map> response = restTemplate.exchange(
				env.getProperty("wistia.createProjectUrl"), HttpMethod.POST,
				new HttpEntity<Map<String, String>>(map, headers), Map.class);
		Map<String, Object> result = response.getBody();
		WistiaProject wistiaProject = new WistiaProject();
		wistiaProject.setWistiaId(new Long((Integer) result.get("id")));
		wistiaProject.setHashedId((String) result.get("hashedId"));
		return wistiaProject;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configureRestTemplate();

	}
}
