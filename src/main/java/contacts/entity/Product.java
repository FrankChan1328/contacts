package contacts.entity;

public class Product {
	private Long id;
	 
	// 商品类型
	private String productType;
	 
	// 库存量
	private Long storageCount;
	  
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Long getStorageCount() {
		return storageCount;
	}

	public void setStorageCount(Long storageCount) {
		this.storageCount = storageCount;
	}


}
