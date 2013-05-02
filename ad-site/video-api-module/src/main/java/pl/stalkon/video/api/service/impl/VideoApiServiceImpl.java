package pl.stalkon.video.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.WistiaProject;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.video.api.service.VideoApiException;
import pl.stalkon.video.api.service.VideoApiService;
import pl.stalkon.video.api.youtube.InvalidYoutubeUrlException;
import pl.stalkon.video.api.youtube.YoutubeApiService;

@Service
public class VideoApiServiceImpl implements VideoApiService{
	
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
	public Ad setVideoDetails(AdPostDto adPostDto) throws VideoApiException {
		if(adPostDto.getUrl() != null){
			try{
			return youtubeApiService.setVideoDetails(adPostDto);
			}catch(InvalidYoutubeUrlException e){
				throw new VideoApiException(messageSource.getMessage("error.invalidYoutubeUrl", new Object[]{}, LocaleContextHolder.getLocale()));
			}
		}
		else
			return wistiaApiService.setVideoDetails(adPostDto);
	}

	@Override
	public void setApiData(Ad ad) {
		if(ad.getWistiaVideo() != null)
		wistiaApiService.setApiData(ad);
	}


//	@Override
//	public Brand setBrandDetails(Brand brand) {
//		WistiaProject project = wistiaApiService.createWistiaProjecj(brand);
//		brand.setWistiaProject(project);
//		return brand;
//	}

	

}
