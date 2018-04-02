package com.synectiks.demo.site.dto;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class BillingDTO implements DemoDTO {

	private static final long serialVersionUID = 11684900185520264L;

	private String id;
	private String cardHolder;
	private String cardNumber;
	private String expiryDate;
	private String securityCode;

	private String customerId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardHolder() {
		return cardHolder;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getSecurityCode() {
		return securityCode;
	}

	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public Class<?> getEntityClass() {
		return BillingAddress.class;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (!IUtils.isNullOrEmpty(id)) {
			builder.append("\"id\": \"" + id + "\"");
		}
		if (!IUtils.isNullOrEmpty(cardHolder)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"cardHolder\": \"" + cardHolder + "\"");
		}
		if (!IUtils.isNullOrEmpty(cardNumber)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"cardNumber\": \"" + cardNumber + "\"");
		}
		if (!IUtils.isNullOrEmpty(customerId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"customerId\": \"" + customerId + "\"");
		}
		if (!IUtils.isNullOrEmpty(expiryDate)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"expiryDate\": \"" + expiryDate + "\"");
		}
		builder.append("}");
		return builder.toString();
	}
}
