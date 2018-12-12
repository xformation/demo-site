/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class CartRepository extends DynamoDbRepository<Cart, String> {

	public CartRepository() {
		super(Cart.class);
	}

}
