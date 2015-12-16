package contacts.respsitory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import contacts.entity.Product;
import contacts.mapper.ProductMapper;
import contacts.respsitory.ProductRepository;

@Repository
public class ProducRespositoryImpl implements ProductRepository{

	@Autowired
	private ProductMapper productMapper;
	
	@Override
	@Transactional
	public Long getProductStorageCountByType(String productType) {
		Long storageCount = productMapper.getProductStorageCountByType(productType);
		return storageCount;
	}

	@Override
	@Transactional
	public void updateProduct(Product product) {
		productMapper.updateProduct(product);
	}

	@Override
	@Transactional
	@Cacheable(value = "productcache",keyGenerator = "wiselyKeyGenerator")
	public Product getProductById(Long id) {
		Product product = productMapper.getProductById(id);
		return product;
	}

}
