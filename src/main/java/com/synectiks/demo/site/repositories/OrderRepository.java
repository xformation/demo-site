/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class OrderRepository extends DynamoDbRepository<CustomerOrder, String> {

	public OrderRepository() {
		super(CustomerOrder.class);
	}

}
