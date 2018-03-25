package com.synectiks.demo.site.dto;

import com.synectiks.commons.entities.demo.CartItem;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class CartItemDTO implements DemoDTO {

	private static final long serialVersionUID = -5425701023632251L;

	private String id;
	private int quantity;
	private String productId;
	private double totalPrice;
	private ProductDTO product;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public Class<?> getEntityClass() {
		return CartItem.class;
	}

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
		if (!IUtils.isNull(product)) {
			this.productId = product.getId();
		}
	}
}
