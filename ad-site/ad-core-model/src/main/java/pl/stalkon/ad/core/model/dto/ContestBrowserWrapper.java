package pl.stalkon.ad.core.model.dto;

import java.util.List;

import pl.stalkon.ad.core.model.Contest;


public class ContestBrowserWrapper {
	private final List<Contest> contests;
	private final Long total;

	public ContestBrowserWrapper(List<Contest> contests, Long total) {
		super();
		this.contests = contests;
		this.total = total;
	}

	public List<Contest> getContests() {
		return contests;
	}

	public Long getTotal() {
		return total;
	}

}
