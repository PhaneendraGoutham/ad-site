package pl.stalkon.ad.sitemap;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

public class StaticSitemapWriterTasklet implements Tasklet {

	@Autowired
	private WebSitemapGeneratorWrapper sitemapGeneratorWrapper;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		sitemapGeneratorWrapper.getGenerator().addUrl(sitemapGeneratorWrapper.getBaseUrl() + "#!/glowna");
		sitemapGeneratorWrapper.getGenerator().addUrl(sitemapGeneratorWrapper.getBaseUrl() + "#!/poczekalnia");
		sitemapGeneratorWrapper.getGenerator().addUrl(sitemapGeneratorWrapper.getBaseUrl() + "#!/konkursy");
		sitemapGeneratorWrapper.getGenerator().addUrl(sitemapGeneratorWrapper.getBaseUrl() + "#!/marki");
		return RepeatStatus.FINISHED;
	}
	
}
