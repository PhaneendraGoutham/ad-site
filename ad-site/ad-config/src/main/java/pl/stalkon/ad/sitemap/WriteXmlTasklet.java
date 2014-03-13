package pl.stalkon.ad.sitemap;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.redfin.sitemapgenerator.SitemapIndexGenerator;
import com.redfin.sitemapgenerator.SitemapIndexUrl;

public class WriteXmlTasklet implements Tasklet {

	@Autowired
	private WebSitemapGeneratorWrapper sitemapGeneratorWrapper;
	
	private String sitemapFolder = "web/";

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		List<File> baseSitemaps = sitemapGeneratorWrapper.getGenerator()
				.write();
		if (baseSitemaps.size() == 1) {
			writeIndexFileFromOneSitemap("base_sitemap_index.xml", baseSitemaps
					.get(0).getName());
		} else {
			sitemapGeneratorWrapper.getGenerator().writeSitemapsWithIndex();
			renameSitemapIndexFile("sitemap_index.xml",
					"base_sitemap_index.xml",
					sitemapGeneratorWrapper.getBasePath());
		}
		List<File> videoSitemaps = sitemapGeneratorWrapper.getVideoGenerator()
				.write();
		if (videoSitemaps.size() == 1) {
			writeIndexFileFromOneSitemap("video_sitemap_index.xml",
					videoSitemaps.get(0).getName());
		} else {
			sitemapGeneratorWrapper.getVideoGenerator()
					.writeSitemapsWithIndex();
			renameSitemapIndexFile("sitemap_index.xml",
					"video_sitemap_index.xml",
					sitemapGeneratorWrapper.getBasePath());
		}
		sitemapGeneratorWrapper.build();
		return RepeatStatus.FINISHED;
	}

	private void renameSitemapIndexFile(String nameToChange, String name,
			String basePath) {
		File indexFile = new File(basePath + "/" + nameToChange);
		File newIndexFile = new File(basePath + "/" + name);
		indexFile.renameTo(newIndexFile);
	}

	private void writeIndexFileFromOneSitemap(String name, String sitemapName)
			throws MalformedURLException {
		File file = new File(sitemapGeneratorWrapper.getBasePath() + "/" + name);
		SitemapIndexGenerator sig;
		try {
			sig = new SitemapIndexGenerator.Options(
					sitemapGeneratorWrapper.getBaseUrl() + sitemapFolder, file).build();
		} catch (MalformedURLException e) {
			throw new RuntimeException("bug", e);
		}
		sig.addUrl(new SitemapIndexUrl(new URL(sitemapGeneratorWrapper
				.getBaseUrl() + sitemapFolder  + sitemapName)));
		sig.write();
	}

	public void setSitemapFolder(String sitemapFolder) {
		this.sitemapFolder = sitemapFolder;
	}
}
