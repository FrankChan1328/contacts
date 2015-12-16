package contacts.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import contacts.entity.Product;

public interface ProductMapper {
	
	@Select("SELECT storageCount FROM product WHERE productType = #{productType}")
	Long getProductStorageCountByType(String productType);
	
	@Select("SELECT id, productType, storageCount FROM product WHERE id = #{id}")
	Product getProductById(Long id);
	
	@Update("UPDATE product SET storageCount=storageCount - 1 ")
	void updateProduct(Product product);
}
