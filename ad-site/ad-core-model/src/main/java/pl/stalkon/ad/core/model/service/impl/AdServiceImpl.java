package pl.stalkon.ad.core.model.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.stalkon.ad.core.model.Ad;
import pl.stalkon.ad.core.model.dao.AdDao;
import pl.stalkon.ad.core.model.service.AdService;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserDao;

@Service("adService")
public class AdServiceImpl implements AdService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AdDao adDao;
	
	@Override
	public Ad register(Ad ad, Long id) {
		User poster = userDao.get(id);
		ad.setPoster(poster);
		adDao.add(ad);
		return ad;
	}
	
	

}
