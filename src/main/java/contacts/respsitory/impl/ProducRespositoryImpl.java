package contacts.respsitory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import contacts.entity.Product;
import contacts.mapper.ProductMapper;
import contacts.respsitory.ProductRepository;

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

}
