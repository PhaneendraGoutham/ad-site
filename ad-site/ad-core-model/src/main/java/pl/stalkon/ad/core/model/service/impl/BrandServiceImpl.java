package pl.stalkon.ad.core.model.service.impl;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.DaoQueryObject;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.styall.library.core.model.defaultimpl.UserDao;

@Service("brandService")
public class BrandServiceImpl implements BrandService {


	@Autowired
	private BrandDao brandDao;

	@Transactional
	@Override
	public Brand register(Brand brand, Long id) {
		brandDao.add(brand);
		return brand;
	}

	@Transactional
	@Override
	public Brand get(Long id) {
		return brandDao.get(id);
	}
	@Transactional
	@Override
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last ){
		return brandDao.get(queryObjectList, order, first, last);
	}

}
