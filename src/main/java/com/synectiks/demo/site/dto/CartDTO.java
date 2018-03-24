package com.synectiks.demo.site.dto;

import java.util.ArrayList;
import java.util.List;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class CartDTO implements DemoDTO {

	private static final long serialVersionUID = -181789378356642L;

	private String id;
	private List<String> cartItems;
	private String customerId;
	private double grandTotal;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<String> cartItems) {
		this.cartItems = cartItems;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void addAnItem(CartItemDTO item) {
		if (IUtils.isNull(this.cartItems)) {
			this.cartItems = new ArrayList<>();
		}
		if (!cartItems.contains(item.getId())) {
			this.cartItems.add(item.getId());
			this.grandTotal += item.getTotalPrice();
		}
	}

	@Override
	public Class<?> getEntityClass() {
		return Cart.class;
	}
}
