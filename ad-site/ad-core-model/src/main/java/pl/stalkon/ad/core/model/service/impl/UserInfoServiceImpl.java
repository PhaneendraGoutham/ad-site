package pl.stalkon.ad.core.model.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.UserInfo;
import pl.stalkon.ad.core.model.UserInfo.Type;
import pl.stalkon.ad.core.model.dao.UserDao;
import pl.stalkon.ad.core.model.dao.UserInfoDao;
import pl.stalkon.ad.core.model.service.UserInfoService;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;
	
	@Autowired
	private UserDao userDao;
	
	
	@Override
	@Transactional
	public int getUserMessagesCount(Long userId) {
		Long count = userInfoDao.getUserInfoCount(userId);
		return count.intValue();
	}
	
	@Override
	@Transactional
	public List<UserInfo> getMessages(Long userId, Boolean handled) {
		List<UserInfo> userInfos = userInfoDao.getMessages(userId, handled);
		return userInfos;
	}
	@Override
	@Transactional
	public UserInfo getWithContest(Long userInfoId){
		UserInfo userInfo = userInfoDao.get(userInfoId);
		Hibernate.initialize(userInfo.getContest());
		return userInfo;
	}
	
	@Override
	@Transactional
	public boolean setHandled(Long userInfoId, boolean handled, Type type){
		return userInfoDao.setHandled(userInfoId, handled, type);
	}
	

}
