package pl.stalkon.ad.sitemap;

import java.util.Date;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.Brand;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import com.redfin.sitemapgenerator.WebSitemapUrl.Options;

public class BrandSitemapWriter implements ItemWriter<Brand> {

	@Autowired
	private WebSitemapGeneratorWrapper generatorWrapper;

	
	@Autowired
	private PhantomJSCaller phantomJSCaller;
	
	@Override
	public void write(List<? extends Brand> brands) throws Exception {
		String url;
		for (Brand brand : brands) {
			url = generatorWrapper.getBaseUrl() + "#!/marki/" + brand.getId()
					+ "/" + brand.getName().replace(" ", "-");
			generatorWrapper.getGenerator().addUrl(
					new WebSitemapUrl(new Options(url).changeFreq(
							ChangeFreq.WEEKLY).lastMod(new Date())));
			phantomJSCaller.write("marki/" + brand.getId() + "/title");
		}
	}

}
