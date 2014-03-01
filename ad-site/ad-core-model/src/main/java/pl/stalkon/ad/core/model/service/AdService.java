package pl.stalkon.ad.core.model.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Ad.Place;
import pl.stalkon.ad.core.model.AdComment;
import pl.stalkon.ad.core.model.Tag;
import pl.stalkon.ad.core.model.VideoData;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.AdPostDto;
import pl.stalkon.ad.core.model.dto.AdSearchDto;
import pl.stalkon.ad.core.model.dto.AutocompleteDto;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;

public interface AdService {
	public Ad register(AdPostDto adPostDto, Ad ad, Long userId,
			boolean official);

//	public AdBrowserWrapper get(List<DaoQueryObject> queryObjectList,
//			Order order, Integer first, Integer last);

	public AdBrowserWrapper get(AdSearchDto adSearchDto, Paging paging, boolean approved);
	public AdBrowserWrapper getWaiting(AdSearchDto adSearchDto, Paging paging, boolean approved);
	public AdBrowserWrapper getMain(AdSearchDto adSearchDto, Paging paging, boolean approved);

	public Ad get(Long id, boolean approved);

	public void vote(Long adId, Long userId, Short value);

	public AdComment comment(Long adId, Long userId, Long commentId,
			String message);

	public List<AdComment> getComments(Long adId);
//
//	public AdBrowserWrapper getMain(Integer pageFrom, Integer pageCount,
//			boolean approved);
//
//	public AdBrowserWrapper getWaiting(Integer pageFrom, Integer pageCount,
//			boolean approved);

//	public AdBrowserWrapper getUserAds(Long userId, Integer pageFrom,
//			Integer pageCount, boolean approved);
//
//	public AdBrowserWrapper getBrandAds(Long brandId, Integer pageFrom,
//			Integer pageCount, boolean approved);

//	public AdBrowserWrapper getContestAds(Long contestId, Integer pageFrom,
//			Integer pageCount, boolean approved);

	public List<Ad> getTop(Paging paging, Date date);
	public List<Ad> getList(AdSearchDto adSearchDto, Paging paging, boolean approved);

	public boolean isOwner(Long adId, Long userId);

	public Ad update(AdPostDto adPostDto, Long adId, Long userId);
	void setVideoData(Long adId, VideoData videoData);

	public void changeApproval(Long id, boolean approved);

	public void changeAgeProtected(Long id, boolean ageProtected);

	public void changePlace(Long id, Place place);

	public Ad rand();

	public List<Tag> getTags();

	public List<AutocompleteDto> getTagsByTerm(String term);

	public void informAd(Long id, String message);

	public void informComment(Long id, String message);

	public List<Map<String, Object>> getRatings(List<Long> ids);
	
	public Long getBrandAdsCount(Long brandId);
	public Ad getByWistiaVideoId(String wistiaVideoId);
}
