package contacts.respsitory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import contacts.entity.ProductOrder;
import contacts.mapper.ProductOrderMapper;
import contacts.respsitory.ProductOrderRespository;

public class ProductOrderRespositoryImpl implements ProductOrderRespository{

	@Autowired 
	private ProductOrderMapper productOrderMapper;
	
	@Override
	@Transactional
	public void insertProductOrder(ProductOrder order) {
		productOrderMapper.insertProductOrder(order);
	}

}
