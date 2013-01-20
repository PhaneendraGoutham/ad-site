package pl.stalkon.ad.core.model.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.styall.library.core.model.defaultimpl.User;
import pl.styall.library.core.model.defaultimpl.UserDao;

@Service("adService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private BrandDao adDao;

	@Transactional
	@Override
	public Brand register(Brand brand, Long id) {
		User poster = userDao.get(id);
		adDao.add(brand);
		return brand;
	}

	@Transactional
	@Override
	public Brand get(Long id) {
		return adDao.get(id);
	}

}
