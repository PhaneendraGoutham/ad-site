package pl.stalkon.ad.core.model.service.impl.helper;

public class Paging {

	public final Integer from;
	public final Integer perPage;

	public Paging(int page, int perPage) {
		page--;
		this.from = page * perPage;
		this.perPage = perPage;
	}

	public Paging(int perPage) {
		this.perPage = perPage;
		this.from = null;
	}

	public Paging() {
		this.perPage = null;
		this.from = null;
	}
}
