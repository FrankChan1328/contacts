package contacts.mapper;

import org.apache.ibatis.annotations.Insert;
import contacts.entity.ProductOrder;

public interface ProductOrderMapper {
	
	@Insert("INSERT into productorder(userId, productId, createTime) VALUES(#{userId}, #{productId}, #{createTime})")
	void insertProductOrder(ProductOrder order);
}
