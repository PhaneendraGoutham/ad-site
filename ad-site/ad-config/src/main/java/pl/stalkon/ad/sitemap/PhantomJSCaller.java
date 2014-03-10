package pl.stalkon.ad.sitemap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ItemWriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import pl.styall.library.core.model.CommonEntity;

public class PhantomJSCaller {

	private static final String PHANTOMJS_URL_PREFIX = "http://localhost:";
	private static final String PHANTOMJS_URL_SUFIX = "/?_escaped_fragment_=/";
	private String port = "8888";

	private final RestTemplate restTemplate;

	private static final Logger log = Logger.getLogger(PhantomJSCaller.class);

	public PhantomJSCaller() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(1 * 1000);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public void write(String urlPart) throws Exception {
		String url = PHANTOMJS_URL_PREFIX + port + PHANTOMJS_URL_SUFIX
				+ urlPart;
		try {
			String response = restTemplate.getForObject(url, String.class);
			if (response.equals("fail")) {
				throw new PhantomJSFailResponseException(
						"PhantomJS returned fail response on " + urlPart);
			}
		} catch (Exception exception) {
			String closeUrl = PHANTOMJS_URL_PREFIX + port + "/closePhantomJS";
			try {
				restTemplate.getForObject(closeUrl, String.class);
				throw exception;
			} catch (ResourceAccessException ex) {
				log.fatal("PhantomJS closing failed");
				throw exception;
			}
		}
	}

	public void setPort(String port) {
		this.port = port;
	}
}