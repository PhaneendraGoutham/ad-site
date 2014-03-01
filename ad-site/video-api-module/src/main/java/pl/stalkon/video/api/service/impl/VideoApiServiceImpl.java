package pl.stalkon.video.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.VideoData.Provider;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.video.api.service.VideoApiService;
import pl.stalkon.video.api.youtube.YoutubeApiService;

@Service
public class VideoApiServiceImpl implements VideoApiService {

	@Autowired
	private WistiaApiService wistiaApiService;

	@Autowired
	private YoutubeApiService youtubeApiService;

	@Autowired
	private MessageSource messageSource;

	@Override
	public String getAuthorization() {
		return null;
	}

	@Override
	public Ad setVideoDetails(AdPostDto adPostDto){
		if (adPostDto.getUrl() != null) {
			return youtubeApiService.setVideoDetails(adPostDto);
		} else
			return wistiaApiService.setVideoDetails(adPostDto);
	}

	@Override
	public void setApiData(Ad ad) {
		if (ad.getVideoData().getProvider() == Provider.WISTIA)
			wistiaApiService.setApiData(ad);
	}

	@Override
	public boolean deleteVideo(String hashedId) {
		return wistiaApiService.deleteVideo(hashedId);
	}

	// @Override
	// public Brand setBrandDetails(Brand brand) {
	// WistiaProject project = wistiaApiService.createWistiaProjecj(brand);
	// brand.setWistiaProject(project);
	// return brand;
	// }

}
