package pl.stalkon.ad.sitemap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.GoogleVideoSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.VideoData.Provider;

public class AdSitemapWriter implements ItemWriter<Ad> {


	@Autowired
	private WebSitemapGeneratorWrapper generatorWrapper;


	@Override
	public void write(List<? extends Ad> ads) throws Exception {
		String url;
		for (Ad ad : ads) {
			url = generatorWrapper.getBaseUrl() + "#!/reklamy/" + ad.getId() + "/"
					+ ad.getTitle().replace(" ", "-");
			generatorWrapper.getGenerator().addUrl(
					new WebSitemapUrl(new Options(url).changeFreq(
							ChangeFreq.ALWAYS).lastMod(new Date())));
			generatorWrapper.getVideoGenerator().addUrl(
					new GoogleVideoSitemapUrl(getSitemapVideoOptions(ad, url)));
			
		}
	}

	private com.redfin.sitemapgenerator.GoogleVideoSitemapUrl.Options getSitemapVideoOptions(
			Ad ad, String url) throws MalformedURLException {
		List<String> videoTags = new ArrayList<String>(ad.getTags().size() + 1);
		videoTags.addAll(ad.getTagStrings());
		videoTags.add(ad.getBrand().getName());
		return new com.redfin.sitemapgenerator.GoogleVideoSitemapUrl.Options(
				new URL(url), new URL(ad.getVideoData().getVideoUrl()), true)
				.thumbnailUrl(
						new URL(ad.getVideoData().getBigThumbnail())).rating(ad.getRank())
				.category("reklamy").tags(videoTags).title("Spotnik.pl - reklamy: " + ad.getTitle())
				.description(ad.getDescription()).changeFreq(ChangeFreq.ALWAYS)
				.publicationDate(ad.getCreationDate());
	}

}
