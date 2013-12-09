package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.ContestAnswer;

public class ContestAnswerBrowserWrapper extends BrowserWrapper<ContestAnswer> {

	public ContestAnswerBrowserWrapper(List<ContestAnswer> resultList,
			Long total) {
		super(resultList, total);
	}


}
