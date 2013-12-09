package pl.stalkon.ad.core.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAnswer;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;

@Service
public interface ContestService {

	public Contest register(Long userId, ContestPostDto contestPostDto);

	public void setContestImage(String imageUrl, Long contestId);

	public ContestBrowserWrapper get(Paging paging);

	public Contest get(Long contestId);

	public Contest getWithAnswers(Long contestId);
	
	public Contest getWithContestAds(Long contestId);

	// public Contest getWithAds(Long contestId);

	public ContestAnswer registerAnswer(Long userId, Long contestId,
			String answer);

	// public void registerAdAnswer(Ad ad, Long contestId);
	public boolean hasUserPostedAnswer(Long userId, Long contestId);

	public boolean hasUserPostedAd(Long userId, Long contestId);

	public boolean isContestBrand(Long brandId, Long contestId);

	public boolean isContestAdmin(Long userId, Long contestId);

	public Contest getWithBrand(Long contestId);

	public ContestBrowserWrapper getByBrand(Long brandId, Paging paging);

	public void changeAdWinnerState(Long contestAdId, boolean winner);

	public void changeAnswerWinnerState(Long contestAnswerId, boolean winner);

	public ContestAnswerBrowserWrapper getContestAnswers(Long contestId,Paging paging);
	public Contest upadate(Long contestId, ContestPostDto contestPostDto);
	
	public void score(Long contestId);
	
	public List<User> setWinnerUserInfo(List<Ad> ads, Long contestId);
	
	public Long getBrandContestCount(Long brandId);
	
}
