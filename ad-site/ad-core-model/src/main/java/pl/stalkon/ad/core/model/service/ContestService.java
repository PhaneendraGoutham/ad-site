package pl.stalkon.ad.core.model.service;

import org.springframework.stereotype.Service;

import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAnswer;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;

@Service
public interface ContestService {

	public Contest register(Long userId, ContestPostDto contestPostDto);

	public void setContestImage(String imageUrl, Long contestId);

	public ContestBrowserWrapper get(int first, int last);

	public Contest get(Long contestId);

	public Contest getWithAnswers(Long contestId);

	// public Contest getWithAds(Long contestId);

	public ContestAnswer registerAnswer(Long userId, Long contestId,
			String answer);

	// public void registerAdAnswer(Ad ad, Long contestId);
	public boolean hasUserPostedAnswer(Long userId, Long contestId);

	public boolean hasUserPostedAd(Long userId, Long contestId);

	public boolean isContestBrand(Long brandId, Long contestId);

	public boolean isContestAdmin(Long userId, Long contestId);

	public Contest getWithBrand(Long contestId);

	public ContestBrowserWrapper getByBrand(Long brandId, int first, int last);

	public void changeAdWinnerState(Long contestAdId, boolean winner);

	public void changeAnswerWinnerState(Long contestAnswerId, boolean winner);

	public ContestAnswerBrowserWrapper getContestAnswers(Long contestId,
			int first, int last);
}
