package contacts.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import contacts.entity.Product;

public interface ProductMapper {
	
	@Select("SELECT storageCount FROM contacts WHERE productType = #{productType}")
	Long getProductStorageCountByType(String productType);
	
	@Update("UPDATE product SET storageCount=storageCount - 1 ")
	void updateProduct(Product product);
}
