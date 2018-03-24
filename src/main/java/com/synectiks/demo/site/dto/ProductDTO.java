package com.synectiks.demo.site.dto;

import org.springframework.web.multipart.MultipartFile;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.utils.IUtils;

/**
 * @author Rajesh
 */
public class ProductDTO implements DemoDTO {

	private static final long serialVersionUID = -5275312162727880L;

	private String id;
	private String name;
	private String description;
	private String category;
	private String condition;
	private String manufacturer;
	private int stockCount;
	private double price;
	private MultipartFile image;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getStockCount() {
		return stockCount;
	}

	public void setStockCount(int stockCount) {
		this.stockCount = stockCount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	@Override
	public Class<?> getEntityClass() {
		return Product.class;
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
		if (!IUtils.isNullOrEmpty(description)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"description\": \"" + description + "\"");
		}
		if (!IUtils.isNullOrEmpty(category)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"category\": \"" + category + "\"");
		}
		if (!IUtils.isNullOrEmpty(condition)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"condition\": \"" + condition + "\"");
		}
		if (!IUtils.isNullOrEmpty(manufacturer)) {
			builder.append(builder.length() > 2 ? ", " : "");
			builder.append("\"manufacturer\": \"" + manufacturer + "\"");
		}
		builder.append(builder.length() > 2 ? ", " : "");
		builder.append("\"stockCount\": " + stockCount);
		builder.append(", \"price\": " + price);
		builder.append("}");
		return builder.toString();
	}

}
