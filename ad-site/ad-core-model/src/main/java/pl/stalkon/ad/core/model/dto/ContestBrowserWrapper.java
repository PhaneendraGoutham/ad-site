package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.Contest;


public class ContestBrowserWrapper extends BrowserWrapper<Contest> {

	public ContestBrowserWrapper(List<Contest> resultList, Long total) {
		super(resultList, total);
	}

}
