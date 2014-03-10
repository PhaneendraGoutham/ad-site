package pl.stalkon.ad.sitemap;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;


public class PhantomJsRunnerTasklet implements Tasklet {

	// private final String phantomjsAbsolutePath;
	private String port = "8888";
	private String appDomain = "http://localhost:8080/";
	// private final String pagesFilesDirectory;
	private final String command;
	private final String logPath;

	public PhantomJsRunnerTasklet(String phantomjsAbsolutePath,
			String pagesFilesDirectory, String angularServerAbsolutePath, String logPath) {
		// this.phantomjsAbsolutePath = phantomjsAbsolutePath;
		// this.port = port;
		// this.appDomain = appDomain;
		// this.pagesFilesDirectory = pagesFilesDirectory;
		this.logPath = logPath;
		this.command = phantomjsAbsolutePath + " " + angularServerAbsolutePath
				+ " " + port + " " + appDomain + " " + pagesFilesDirectory;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy_MM_dd");
		Date now = Calendar.getInstance().getTime();        
		String logString = " "  + logPath + "/phantom." + df.format(now) + ".out";
		CommandLine cmdLine = CommandLine.parse(command + logString);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(60*1000);
		Executor executor = new DefaultExecutor();
		executor.setExitValue(1);
		executor.setWatchdog(watchdog);
		executor.execute(cmdLine, resultHandler);
		Thread.sleep(500);
		return RepeatStatus.FINISHED;
	}

	public void setAppDomain(String appDomain) {
		this.appDomain = appDomain;
	}

	public void setPort(String port) {
		this.port = port;
	}
}