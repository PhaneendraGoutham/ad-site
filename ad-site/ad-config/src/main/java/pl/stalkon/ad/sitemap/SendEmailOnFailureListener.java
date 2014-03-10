package pl.stalkon.ad.sitemap;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import pl.stalkon.ad.core.model.service.MailService;

public class SendEmailOnFailureListener implements JobExecutionListener {

	@Autowired
	private MailService mailService;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getExitStatus().compareTo(ExitStatus.FAILED) == 0) {

			StringBuilder builder = new StringBuilder();
			builder.append(
					"Job " + jobExecution.getJobInstance().getJobName()
							+ " FAILED AT " + jobExecution.getEndTime())
					.append("<br/>");
			for (Throwable ex : jobExecution.getAllFailureExceptions()) {
				builder.append(ex.getMessage());
				builder.append("<br/>");
			}
			mailService.sendAlertToAdmin("Spring batch failure",
					builder.toString());
		}

	}
}
