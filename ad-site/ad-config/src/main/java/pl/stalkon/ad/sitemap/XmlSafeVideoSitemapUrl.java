package pl.stalkon.ad.sitemap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;

import com.redfin.sitemapgenerator.GoogleVideoSitemapUrl;
import com.redfin.sitemapgenerator.GoogleVideoSitemapUrl.Options;

public class XmlSafeVideoSitemapUrl extends GoogleVideoSitemapUrl {

	public XmlSafeVideoSitemapUrl(URL url, URL playerUrl, boolean allowEmbed) {
		super(url, playerUrl, allowEmbed);
	}

	public XmlSafeVideoSitemapUrl(Options options) {
		super(options);
	}

	public XmlSafeVideoSitemapUrl(URL url, URL contentUrl) {
		super(url, contentUrl);
	}

	/** Retrieves the {@link Options#playerUrl}*/
	public URL getPlayerUrl() {
		if(super.getPlayerUrl() == null) return null;
		String newString = StringEscapeUtils.escapeXml(super.getPlayerUrl().toString());
		URL newUrl = null;
		try {
			newUrl = new URL(newString);
		} catch (MalformedURLException e) {
			// should never get here
		}
		return newUrl;
	}
	
	/** Retrieves the {@link Options#contentUrl}*/
	public URL getContentUrl() {
		if(super.getContentUrl() == null) return null;
		String newString = StringEscapeUtils.escapeXml(super.getContentUrl().toString());
		URL newUrl = null;
		try {
			newUrl = new URL(newString);
		} catch (MalformedURLException e) {
			// should never get here
		}
		return newUrl;
	}

	/** Retrieves the {@link Options#thumbnailUrl}*/
	public URL getThumbnailUrl() {
		if(super.getThumbnailUrl() == null) return null;
		String newString = StringEscapeUtils.escapeXml(super.getThumbnailUrl().toString());
		URL newUrl = null;
		try {
			newUrl = new URL(newString);
		} catch (MalformedURLException e) {
			// should never get here
		}
		return newUrl;
	}
	
	/** Retrieves the {@link Options#title} */
	public String getTitle() {
		if(super.getTitle() == null) return null;
		return StringEscapeUtils.escapeXml(super.getTitle());
	}

	/** Retrieves the {@link Options#description} */
	public String getDescription() {
		if(super.getDescription() == null) return null;
		return StringEscapeUtils.escapeXml(super.getDescription());
	}

	/** Retrieves the {@link Options#tags} */
	public ArrayList<String> getTags() {
		if(super.getTags() == null) return null;
		ArrayList<String> newList = new ArrayList<String>(super.getTags().size());
		for(String tag : super.getTags()){
			newList.add(StringEscapeUtils.escapeXml(tag));
		}
		return newList;
	}

	/** Retrieves the {@link Options#category} */
	public String getCategory() {
		if(super.getCategory() == null) return null;
		return StringEscapeUtils.escapeXml(super.getCategory());
	}

}
