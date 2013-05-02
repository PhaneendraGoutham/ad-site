package pl.stalkon.video.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.dto.AdPostDto;


public interface VideoApiService {

	public String getAuthorization();
	public Ad setVideoDetails(AdPostDto adPostDto)  throws VideoApiException;
	public void setApiData(Ad ad);
	
	
//	public Brand setBrandDetails(Brand brand);
}
