/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class BillingRepository extends DynamoDbRepository<BillingAddress, String> {

	public BillingRepository() {
		super(BillingAddress.class);
	}

}
