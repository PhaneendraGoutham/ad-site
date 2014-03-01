package pl.stalkon.ad.sitemap;

import java.io.File;
import java.net.MalformedURLException;

import com.redfin.sitemapgenerator.GoogleVideoSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapGenerator;

public class WebSitemapGeneratorWrapper {

	private WebSitemapGenerator currentWebSitemapGenerator;
	private GoogleVideoSitemapGenerator googleVideoSitemapGenerator;
	private final String baseUrl;
	private final String basePath;
	private final int baseMaxUrl;
	private final int videoMaxUrl;

	public WebSitemapGeneratorWrapper(String baseUrl, String basePath)
			throws MalformedURLException {
		this.baseUrl = baseUrl;
		this.basePath = basePath;
		this.baseMaxUrl = 1000;
		this.videoMaxUrl = 1000;
		build();
	}

	public WebSitemapGeneratorWrapper(String baseUrl, String basePath,
			int baseMaxUrl, int videoMaxUrl) throws MalformedURLException {
		this.baseUrl = baseUrl;
		this.basePath = basePath;
		this.baseMaxUrl = baseMaxUrl;
		this.videoMaxUrl = videoMaxUrl;
		build();
	}

	public void build() throws MalformedURLException {
		currentWebSitemapGenerator = WebSitemapGenerator
				.builder(baseUrl, new File(basePath))
				.allowMultipleSitemaps(true).maxUrls(baseMaxUrl).gzip(true)
				.build();
		googleVideoSitemapGenerator = GoogleVideoSitemapGenerator
				.builder(baseUrl, new File(basePath))
				.fileNamePrefix("video-sitemap").allowMultipleSitemaps(true)
				.maxUrls(videoMaxUrl).gzip(true).build();
	}

	public WebSitemapGenerator getGenerator() {
		return currentWebSitemapGenerator;
	}

	public GoogleVideoSitemapGenerator getVideoGenerator() {
		return googleVideoSitemapGenerator;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

}
