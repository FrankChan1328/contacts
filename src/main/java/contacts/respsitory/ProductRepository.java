package contacts.respsitory;

import contacts.entity.Product;

public interface ProductRepository {
	
	Long getProductStorageCountByType(String productType);
	
	void updateProduct(Product product);
}
