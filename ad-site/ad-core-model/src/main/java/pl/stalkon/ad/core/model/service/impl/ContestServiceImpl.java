package pl.stalkon.ad.core.model.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Contest;
import pl.stalkon.ad.core.model.ContestAd;
import pl.stalkon.ad.core.model.Contest.State;
import pl.stalkon.ad.core.model.ContestAnswer;
import pl.stalkon.ad.core.model.User;
import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.UserRoleDef;
import pl.stalkon.ad.core.model.UserInfo.Type;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.ContestAdDao;
import pl.stalkon.ad.core.model.dao.ContestAnswerDao;
import pl.stalkon.ad.core.model.dao.ContestDao;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.dao.UserInfoDao;
import pl.stalkon.ad.core.model.dto.ContestAnswerBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestBrowserWrapper;
import pl.stalkon.ad.core.model.dto.ContestPostDto;
import pl.stalkon.ad.core.model.service.ContestService;
import pl.stalkon.ad.core.model.service.impl.helper.Paging;
import pl.styall.library.core.model.dao.DaoQueryObject;

@Service
public class ContestServiceImpl implements ContestService{

	
	@Autowired
	private ContestDao contestDao;
	
	@Autowired
	private BrandDao brandDao;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserInfoDao userInfoDao;
	
	@Autowired
	private ContestAnswerDao contestAnswerDao;
	
	@Autowired
	private ContestAdDao contestAdDao;
	
	@Override
	@Transactional
	public Contest register(Long userId, ContestPostDto contestPostDto) {
		Contest contest = new Contest();
		User user = userDao.get(userId);
		if(!user.getUserRoles().contains(UserRoleDef.ROLE_CONTEST.value()))
			user.addUserRole(userDao.loadUserRoleByName(UserRoleDef.ROLE_CONTEST.value()));
		contest.setUser(user);
		Brand brand = brandDao.get(contestPostDto.getBrandId());
		contest.setBrand(brand);
		contest.setName(contestPostDto.getName());
		contest.setDescription(contestPostDto.getDescription());
		contest.setType(contestPostDto.getType());
		contest.setState(State.ON_GOING);
		contest.setFinishDate(contestPostDto.getFinishDate());
		contest.setScoresDate(contestPostDto.getScoresDate());
		contest.setActive(true);
		contestDao.add(contest);
		return contest;
	}
	
	@Override
	@Transactional
	public Contest upadate(Long contestId, ContestPostDto contestPostDto) {
		Contest contest = contestDao.get(contestId);
		contest.setName(contestPostDto.getName());
		contest.setDescription(contestPostDto.getDescription());
		contest.setType(contestPostDto.getType());
		contest.setFinishDate(contestPostDto.getFinishDate());
		contest.setScoresDate(contestPostDto.getScoresDate());
		contestDao.update(contest);
		return contest;
	}

	@Override
	@Transactional
	public void setContestImage(String imageUrl, Long contestId) {
		Contest contest = contestDao.get(contestId);
		contest.setImageUrl(imageUrl);
		contestDao.update(contest);
	}

	@Override
	@Transactional
	public ContestBrowserWrapper get(Paging paging) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		queryObjectList.add(new DaoQueryObject("active", true));
		return contestDao.get(queryObjectList, null, paging);
	}
	
	@Override
	@Transactional
	public ContestBrowserWrapper getByBrand(Long brandId, Paging paging) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		queryObjectList.add(new DaoQueryObject("active", true));
		List<DaoQueryObject> brandQueryObjectList = new ArrayList<DaoQueryObject>();
		brandQueryObjectList.add(new DaoQueryObject("id",brandId));
		queryObjectList.add(new DaoQueryObject("brand", brandQueryObjectList));
		return contestDao.get(queryObjectList, null, paging);
	}

	@Override
	@Transactional
	public Contest get(Long contestId) {
		Contest contest = contestDao.get(contestId);
		Hibernate.initialize(contest);
		return contest;
	}

	@Override
	@Transactional
	public Contest getWithAnswers(Long contestId) {
		Contest contest = contestDao.get(contestId);
		contest.getAnswers().size();
		return contest;
	}
//	@Override
//	@Transactional
//	public Contest getWithAds(Long contestId) {
//		Contest contest = contestDao.get(contestId);
//		contest.getAds().size();
//		return contest;
//	}

	@Override
	@Transactional
	public ContestAnswer registerAnswer(Long userId, Long contestId,
			String answer) {
		Contest contest = contestDao.get(contestId);
		User user = userDao.get(userId);
		ContestAnswer contestAnswer = new ContestAnswer();
		contestAnswer.setAnswer(answer);
		contestAnswer.setUser(user);
		contestAnswer.setContest(contest);
		contestAnswerDao.add(contestAnswer);
		return contestAnswer;
	}

	@Override
	@Transactional
	public boolean hasUserPostedAnswer(Long userId, Long contestId) {
		return contestAnswerDao.hasUserPostedAnswer(userId, contestId);
	}
	@Override
	@Transactional
	public boolean hasUserPostedAd(Long userId, Long contestId) {
		return contestAdDao.hasUserPostedAd(userId, contestId);
	}

	@Override
	@Transactional
	public Contest getWithBrand(Long contestId) {
		Contest contest = contestDao.get(contestId);
		Hibernate.initialize(contest.getBrand());
		return contest;
	}

//	@Override
//	@Transactional
//	public void registerAdAnswer(Ad ad, Long contestId) {
//		Contest contest = contestDao.get(contestId);
//		contest.addAd(ad);
//		contestDao.update(contest);
//	}

	@Override
	@Transactional
	public boolean isContestBrand(Long brandId, Long contestId) {
		Contest contest = contestDao.get(contestId);
		if(contest.getBrand().getId().equals(brandId)){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public void changeAdWinnerState(Long contestAdId, boolean winner) {
		ContestAd contestAd = contestAdDao.get(contestAdId);
		contestAd.setWinner(winner);
		contestAdDao.update(contestAd);
	}
	@Override
	@Transactional
	public void changeAnswerWinnerState(Long contestAnswerId, boolean winner) {
		ContestAnswer contestAnswer = contestAnswerDao.get(contestAnswerId);
		contestAnswer.setWinner(winner);
		contestAnswerDao.update(contestAnswer);
	}

	@Override
	@Transactional
	public ContestAnswerBrowserWrapper getContestAnswers(Long contestId, Paging paging) {
		List<DaoQueryObject> queryObjectList = new ArrayList<DaoQueryObject>();
		List<DaoQueryObject> contestQueryObjectList = new ArrayList<DaoQueryObject>();
		contestQueryObjectList.add(new DaoQueryObject("id", contestId));
		queryObjectList.add(new DaoQueryObject("contest", contestQueryObjectList));
		return contestAnswerDao.get(queryObjectList, Order.desc("creationDate"), paging);
		
	}

	@Override
	@Transactional
	public boolean isContestAdmin(Long userId, Long contestId) {
		Contest contest = contestDao.get(contestId);
		if(contest.getUser().getId().equals(userId)){
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public Contest getWithContestAds(Long contestId) {
		Contest contest = contestDao.get(contestId);
		List<ContestAd> contestAds = contestAdDao.getContestAdsWithAds(contestId);
		Hibernate.initialize(contest);
		contest.setContestAds(contestAds);
		return contest;
	}

	@Override
	@Transactional
	public void score(Long contestId) {
		Contest contest = contestDao.get(contestId);
		contest.setState(State.SCORED);
		contestDao.update(contest);
	}
	@Override
	@Transactional
	public void finish(Long contestId) {
		Contest contest = contestDao.get(contestId);
		contest.setState(State.FINISHED);
		contestDao.update(contest);
	}

	@Override
	@Transactional
	public List<User> setWinnerUserInfo(List<Ad> ads, Long contestId) {
		List<User> users = new ArrayList<User>(ads.size());
		for(Ad ad: ads){
			UserInfo userInfo = new UserInfo();
			userInfo.setHandled(false);
			Contest contest = contestDao.get(contestId);
			userInfo.setContest(contest);
			userInfo.setType(Type.CONTEST_WINNER);
			User user = userDao.get(ad.getUser().getId());
			userInfo.setUser(user);
			//TODO: batch insert
			userInfoDao.add(userInfo);
			users.add(ad.getUser());
		}
		return users;
	}

	@Override
	@Transactional
	public Long getBrandContestCount(Long brandId) {
		return contestDao.getBrandContestsCount(brandId);
	}







}
