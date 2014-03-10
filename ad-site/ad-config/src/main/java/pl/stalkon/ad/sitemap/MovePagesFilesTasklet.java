package pl.stalkon.ad.sitemap;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class MovePagesFilesTasklet implements Tasklet {

	private final String from;
	private final String to;
	
	
	public MovePagesFilesTasklet(String from, String to) {
		this.from = from;
		this.to = to;
	}


	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		
		File fileFrom = new File(from);
		File fileTo = new File(to);
		
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		fileTo.renameTo(new File(fileTo.getParent() + "/" + df.format(fileTo.lastModified())));
		fileFrom.renameTo(new File(to));
		
		return RepeatStatus.FINISHED;
	}
	
	
	
	
}
