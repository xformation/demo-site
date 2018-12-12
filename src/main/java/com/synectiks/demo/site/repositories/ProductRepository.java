/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class ProductRepository extends DynamoDbRepository<Product, String> {

	public ProductRepository() {
		super(Product.class);
	}

}
