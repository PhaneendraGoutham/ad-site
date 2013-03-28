package pl.stalkon.ad.core.model.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.Order;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.dao.DaoQueryObject;
import pl.styall.library.core.ext.QueryObject;

public interface BrandService  {
	public Brand register(Brand brand, Long id);
	public Brand get(Long id);
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last );
}
