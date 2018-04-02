package com.synectiks.demo.site.dto;

import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class CustomerDTO implements DemoDTO {

    private static final long serialVersionUID = -203986941939553L;

    private String id;
    private String cartId;
    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;
    private boolean enabled;
    private String billingId;
    private String shippingId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
		return Customer.class;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (!IUtils.isNullOrEmpty(id)) {
			builder.append("\"id\": \"" + id + "\"");
		}
		if (!IUtils.isNullOrEmpty(name)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"name\": \"" + name + "\"");
		}
		if (!IUtils.isNullOrEmpty(phone)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"phone\": \"" + phone + "\"");
		}
		if (!IUtils.isNullOrEmpty(email)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"email\": \"" + email + "\"");
		}
		if (!IUtils.isNullOrEmpty(username)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"username\": \"" + username + "\"");
		}
		if (!IUtils.isNullOrEmpty(cartId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"cartId\": \"" + cartId + "\"");
		}
		if (!IUtils.isNullOrEmpty(billingId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"billingId\": \"" + billingId + "\"");
		}
		if (!IUtils.isNullOrEmpty(shippingId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"shippingId\": \"" + shippingId + "\"");
		}
		builder.append(", \"enabled\": " + enabled);
		builder.append("}");
		return builder.toString();
	}
}
