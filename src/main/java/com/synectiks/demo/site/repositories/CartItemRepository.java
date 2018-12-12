/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.CartItem;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class CartItemRepository extends DynamoDbRepository<CartItem, String> {

	public CartItemRepository() {
		super(CartItem.class);
	}

	/**
	 * Find a customer by username
	 * @param username
	 * @return
	 */
	public CartItem findByProductId(String prodId) {
		Iterable<CartItem> users = super.findByKeyValue("productId", prodId);
		if (!IUtils.isNull(users) && !IUtils.isNull(users.iterator())
				&& users.iterator().hasNext()) {
			return users.iterator().next();
		}
		return null;
	}

}
