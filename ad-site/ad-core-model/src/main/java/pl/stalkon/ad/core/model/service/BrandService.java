package pl.stalkon.ad.core.model.service;

import java.util.List;
import java.util.Map;

import pl.stalkon.ad.core.model.Brand;

public interface BrandService  {
	public Brand register(Brand brand, Long id);
	public Brand get(Long id);
}
