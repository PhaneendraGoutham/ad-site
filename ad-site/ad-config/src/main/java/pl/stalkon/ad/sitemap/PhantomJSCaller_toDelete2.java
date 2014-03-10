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

public class PhantomJSCaller_toDelete2 implements ItemWriter<Long>,
		ItemWriteListener<Long> {

	private static final String PHANTOMJS_URL_PREFIX = "http://localhost:";
	private static final String PHANTOMJS_URL_SUFIX = "/?_escaped_fragment_=/";
	private String port = "8888";

	private final String folder;
	private final RestTemplate restTemplate;
	
	private static final Logger log = Logger.getLogger(PhantomJSCaller_toDelete2.class);

	public PhantomJSCaller_toDelete2(String folder) {
		this.folder = folder;
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(1 * 1000);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	@Override
	public void write(List<? extends Long> items) throws Exception {
		for (Long item : items) {
			String url = PHANTOMJS_URL_PREFIX + port + PHANTOMJS_URL_SUFIX
					+ folder + "/" + item + "/title";
			String response = restTemplate.getForObject(url, String.class);
			if (response.equals("fail")) {
				throw new PhantomJSFailResponseException(
						"PhantomJS returned fail response on " + folder + " "
								+ item);
			}
		}
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public void beforeWrite(List<? extends Long> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterWrite(List<? extends Long> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWriteError(Exception exception, List<? extends Long> items) {
		String url = PHANTOMJS_URL_PREFIX + port + "/closePhantomJS";
		try{
			restTemplate.getForObject(url, String.class);
		}catch(ResourceAccessException ex){
			log.fatal("PhantomJS closing failed");
		}
	}
}