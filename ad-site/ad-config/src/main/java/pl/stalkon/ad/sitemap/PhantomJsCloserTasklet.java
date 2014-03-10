package pl.stalkon.ad.sitemap;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class PhantomJsCloserTasklet implements Tasklet {

	private static final String PHANTOMJS_URL_PREFIX = "http://localhost:";
	private String port = "8888";
	private static final String PHANTOMJS_CLOSE_STRING = "/closePhantomJS";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		restTemplate.getForObject(PHANTOMJS_URL_PREFIX + port + PHANTOMJS_CLOSE_STRING, String.class);
		return RepeatStatus.FINISHED;
	}

	public void setPort(String port) {
		this.port = port;
	}


}
