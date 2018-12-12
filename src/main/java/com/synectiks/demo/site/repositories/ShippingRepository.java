/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class ShippingRepository extends DynamoDbRepository<ShippingAddress, String> {

	public ShippingRepository() {
		super(ShippingAddress.class);
	}

}
