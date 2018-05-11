package com.synectiks.demo.site.dto;

import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class OrderDTO implements DemoDTO {

	private static final long serialVersionUID = 690279501282819L;
	public static final String ORDER_SESSION_KEY = "orderObj";

	private String id;
	private String ssmId;
	private String cartId;
	private String customerId;
	private String billingId;
	private String shippingId;
	private CartDTO cart;
	private BillingDTO billing;
	private CustomerDTO customer;
	private ShippingDTO shipping;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSsmId() {
		return ssmId;
	}

	public void setSsmId(String ssmId) {
		this.ssmId = ssmId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getBillingId() {
		return billingId;
	}

	public void setBillingId(String billingId) {
		this.billingId = billingId;
	}

	public String getShippingId() {
		return shippingId;
	}

	public void setShippingId(String shippingId) {
		this.shippingId = shippingId;
	}

	@Override
	public Class<?> getEntityClass() {
		return CustomerOrder.class;
	}

	public CartDTO getCart() {
		return cart;
	}

	public void setCart(CartDTO cart) {
		this.cart = cart;
	}

	public BillingDTO getBilling() {
		return billing;
	}

	public void setBilling(BillingDTO billing) {
		this.billing = billing;
	}

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public ShippingDTO getShipping() {
		return shipping;
	}

	public void setShipping(ShippingDTO shipping) {
		this.shipping = shipping;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (!IUtils.isNullOrEmpty(id)) {
			builder.append("\"id\": \"" + id + "\"");
		}
		if (!IUtils.isNullOrEmpty(cartId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"cartId\": \"" + cartId + "\"");
		}
		if (!IUtils.isNullOrEmpty(customerId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"customerId\": \"" + customerId + "\"");
		}
		if (!IUtils.isNullOrEmpty(billingId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"billingId\": \"" + billingId + "\"");
		}
		if (!IUtils.isNullOrEmpty(shippingId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"shippingId\": \"" + shippingId + "\"");
		}
		builder.append("}");
		return builder.toString();
	}
}
