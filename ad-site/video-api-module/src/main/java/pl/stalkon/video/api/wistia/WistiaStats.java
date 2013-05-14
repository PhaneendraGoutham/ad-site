package pl.stalkon.video.api.wistia;

import java.util.Date;

public class WistiaStats {

	private Date date;
	private Long loadCount;
	private Long playCount;
	private Double hoursWatched;
	
	public WistiaStats(Date date, Long loadCount, Long playCount,
			Double hoursWatched) {
		super();
		this.date = date;
		this.loadCount = loadCount;
		this.playCount = playCount;
		this.hoursWatched = hoursWatched;
	}

	public Date getDate() {
		return date;
	}

	public Long getLoadCount() {
		return loadCount;
	}

	public Long getPlayCount() {
		return playCount;
	}

	public Double getHoursWatched() {
		return hoursWatched;
	}

}
