package pl.stalkon.ad.social.facebook;

public interface SiteFacebookIntegrator {

	public void notifyOnComment(String link, String title, String description);
	public void notifyOnAdCreated(String link, String title, String description);
	public void notifyOnVote(String link, String title, String description, short rank);
	
	
}
