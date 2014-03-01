package pl.stalkon.video.api.service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dto.AdPostDto;


public interface VideoApiService {

	public String getAuthorization();
	public Ad setVideoDetails(AdPostDto adPostDto);
	public void setApiData(Ad ad);
	
	public boolean deleteVideo(String hashedId);
	
	
//	public Brand setBrandDetails(Brand brand);
}
