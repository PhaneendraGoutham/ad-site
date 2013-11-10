package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.ContestAnswer;

public class ContestAnswerBrowserWrapper {

	private final List<ContestAnswer> answers;
	private final Long total;

	public ContestAnswerBrowserWrapper(List<ContestAnswer> answers, Long total) {
		super();
		this.answers = answers;
		this.total = total;
	}

	public Long getTotal() {
		return total;
	}

	public List<ContestAnswer> getAnswers() {
		return answers;
	}

}
