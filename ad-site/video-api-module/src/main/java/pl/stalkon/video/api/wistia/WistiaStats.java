package pl.stalkon.video.api.wistia;

public class WistiaStats {

	private Long loadCount;
	private Long playCount;
	private Double hoursWatched;
	public WistiaStats(Long loadCount, Long playCount,
			Double hoursWatched) {
		super();
		this.loadCount = loadCount;
		this.playCount = playCount;
		this.hoursWatched = hoursWatched;
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
