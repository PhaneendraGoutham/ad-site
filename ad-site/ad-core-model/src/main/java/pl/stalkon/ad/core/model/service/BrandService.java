package pl.stalkon.ad.core.model.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.stalkon.ad.core.model.Brand;
import pl.styall.library.core.ext.QueryObject;

public interface BrandService  {
	public Brand register(Brand brand, Long id);
	public Brand get(Long id);
	public List<Brand> get(Map<String, Object> queryObject);
}