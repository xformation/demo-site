package com.synectiks.demo.site.dto;

import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class ShippingDTO implements DemoDTO {

	private static final long serialVersionUID = -5524918443977763L;

	private String id;
	private String customerId;
	private String street;
	private String division;
	private String apartmentNumber;
	private String city;
	private String state;
	private String country;
	private String zip;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public Class<?> getEntityClass() {
		return ShippingAddress.class;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		if (!IUtils.isNullOrEmpty(id)) {
			builder.append("\"id\": \"" + id + "\"");
		}
		if (!IUtils.isNullOrEmpty(apartmentNumber)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"apartmentNumber\": \"" + apartmentNumber + "\"");
		}
		if (!IUtils.isNullOrEmpty(city)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"city\": \"" + city + "\"");
		}
		if (!IUtils.isNullOrEmpty(country)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"country\": \"" + country + "\"");
		}
		if (!IUtils.isNullOrEmpty(customerId)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"customerId\": \"" + customerId + "\"");
		}
		if (!IUtils.isNullOrEmpty(division)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"division\": \"" + division + "\"");
		}
		if (!IUtils.isNullOrEmpty(state)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"state\": \"" + state + "\"");
		}
		if (!IUtils.isNullOrEmpty(street)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"street\": \"" + street + "\"");
		}
		if (!IUtils.isNullOrEmpty(zip)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"zip\": \"" + zip + "\"");
		}
		builder.append("}");
		return builder.toString();
	}

}
