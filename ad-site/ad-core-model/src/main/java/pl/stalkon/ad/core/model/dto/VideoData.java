package pl.stalkon.ad.core.model.dto;

public class VideoData {

	public enum Provider{
		YOUTUBE("youtube", "http://i.ytimg.com/vi"),
		WISTIA("wistia", "");
		private final String name;
		private final String thumbnailUrl;
		
		Provider(String name, String thumbnailUrl){
			this.name = name;
			this.thumbnailUrl = thumbnailUrl;
		}
		public String getName(){
			return name;
		}
		public String getThumbnailUrl(){
			return thumbnailUrl;
		}
	}
	
	private Provider provider;
	private String id;
	public VideoData(Provider provider, String id) {
		super();
		this.provider = provider;
		this.id = id;
	}
	public Provider getProvider() {
		return provider;
	}
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	public String getVideoId() {
		return id;
	}
	public void setVideoId(String id) {
		this.id = id;
	}

	

}
