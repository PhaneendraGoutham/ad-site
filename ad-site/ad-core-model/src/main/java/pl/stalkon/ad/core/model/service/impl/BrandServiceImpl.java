package pl.stalkon.ad.core.model.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.stalkon.ad.core.model.Brand;
import pl.stalkon.ad.core.model.Company;
import pl.stalkon.ad.core.model.WistiaProjectData;
import pl.stalkon.ad.core.model.dao.BrandDao;
import pl.stalkon.ad.core.model.dao.CompanyDao;
import pl.stalkon.ad.core.model.dto.AdBrowserWrapper;
import pl.stalkon.ad.core.model.dto.BrandPostDto;
import pl.stalkon.ad.core.model.dto.BrandSearchDto;
import pl.stalkon.ad.core.model.service.BrandService;
import pl.styall.library.core.model.dao.DaoQueryObject;
import pl.styall.library.core.model.dao.DaoQueryObject.CompareType;
import pl.styall.library.core.model.defaultimpl.UserDao;

@Service("brandService")
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandDao brandDao;

	@Autowired
	private CompanyDao companyDao;

//	@Transactional
//	@Override
//	public Brand register(BrandPostDto brandPostDto, WistiaProject wistiaProjectData) {
//		Brand brand = createBrand(brandPostDto, wistiaProjectData);
//		brandDao.add(brand);
//		return brand;
//	}

	@Transactional
	@Override
	public Brand get(Long id) {
		return brandDao.getInitialized(id);
	}

	@Transactional
	@Override
	public List<Brand> get(List<DaoQueryObject> queryObjectList, Order order,
			Integer first, Integer last) {
		return brandDao.get(queryObjectList, order, first, last);
	}

	@Override
	@Transactional
	public List<Brand> get() {
		return brandDao.get();
	}

	@Override
	@Transactional
	public List<BrandSearchDto> getByTerm(String term) {
		List<DaoQueryObject> query = new ArrayList<DaoQueryObject>(1);
		query.add(new DaoQueryObject("name", "%" + term + "%", CompareType.LIKE));
		List<Brand> brands = brandDao.get(query, Order.asc("name"), 0, 5);
		List<BrandSearchDto> result = new ArrayList<BrandSearchDto>(
				brands.size());
		for (Brand brand : brands) {
			result.add(new BrandSearchDto(brand.getName(), brand.getId(), brand
					.getWistiaProjectData().getHashedId()));
		}
		return result;
	}

	@Override
	@Transactional
	public Brand register(BrandPostDto brandPostDto,
			WistiaProjectData wistiaProjectData, Long companyId) {
		Brand brand = createBrand(brandPostDto, wistiaProjectData);
		System.out.println(companyId);
		if (companyId != null) {
			System.out.println("adfasdfsdfasdf");
			Company company = companyDao.get(companyId);
			brand.setCompany(company);
		}
		brandDao.add(brand);
		return brand;
	}

	@Override
	@Transactional
	public void setBrandLogo(String logo,String smallLogo, Long brandId) {
		Brand brand = brandDao.get(brandId);
		brand.setLogoUrlo(logo);
		brand.setSmallLogoUrl(smallLogo);
		brandDao.update(brand);
	}

	

	@Override
	@Transactional
	public Brand update(BrandPostDto brandPostDto, Long brandId) {
		Brand brand = brandDao.get(brandId);
		brand.setName(brandPostDto.getName());
		brand.setDescription(brandPostDto.getDescription());
		brandDao.update(brand);
		return brand;
	}
	
	private Brand createBrand(BrandPostDto brandPostDto,
			WistiaProjectData wistiaProjectData) {
		Brand brand = new Brand();
		brand.setDescription(brandPostDto.getDescription());
		brand.setName(brandPostDto.getName());
		brand.setWistiaProjectData(wistiaProjectData);
		return brand;
	}


}
