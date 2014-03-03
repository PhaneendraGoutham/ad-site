package pl.stalkon.ad.sitemap;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.User;

import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;

public class UserSitemapWriter implements ItemWriter<User> {

	@Autowired
	private WebSitemapGeneratorWrapper generatorWrapper;

	@Override
	public void write(List<? extends User> users) throws Exception {
		String url;
		for (User user : users) {
			url = generatorWrapper.getBaseUrl() + "#!/uzytkownik/"
					+ user.getId();
			generatorWrapper.getGenerator().addUrl(
					new WebSitemapUrl(new Options(url).changeFreq(
							ChangeFreq.ALWAYS).lastMod(new Date())));
		}
	}

}
