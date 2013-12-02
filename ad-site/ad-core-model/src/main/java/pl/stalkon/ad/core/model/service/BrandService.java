package pl.stalkon.ad.core.model.service;

import java.util.List;
import org.hibernate.criterion.Order;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dto.BrandPostDto;
import pl.stalkon.ad.core.model.dto.BrandSearchDto;
import pl.styall.library.core.model.dao.DaoQueryObject;

public interface BrandService  {
//	public Brand register(BrandPostDto brandPostDto, WistiaProject wistiaProjectData);
	public Brand register(BrandPostDto brandPostDto, WistiaProjectData wistiaProjectData, Long companyId);
	public Brand update(BrandPostDto brandPostDto, Long brandId);
	public List<Brand> getCompanyBrands(Long companyId);
	public Brand get(Long id);
	public List<BrandSearchDto> getByTerm(String term);
	
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order, Integer first, Integer last );
	public List<Brand> get();
	
	public void setBrandLogo(String logo,String smallLogo, Long brandId);
	
	public Double getBrandCurrentCost(Long brandId);
}
