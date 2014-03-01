package pl.stalkon.ad.sitemap;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.Contest;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;

public class ContestSitemapWriter implements ItemWriter<Contest> {


	@Autowired
	private WebSitemapGeneratorWrapper generatorWrapper;


	@Override
	public void write(List<? extends Contest> contests) throws Exception {
		String url;
		for (Contest contest : contests) {
			url = generatorWrapper.getBaseUrl() + "#!/konkursy/" + contest.getId() + "/" + contest.getName().replace(" ", "-");
			generatorWrapper.getGenerator().addUrl(
					new WebSitemapUrl(new Options(url).changeFreq(
							ChangeFreq.DAILY).lastMod(new Date())));
		}
	}

}
