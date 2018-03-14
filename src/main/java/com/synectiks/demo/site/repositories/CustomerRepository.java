/**
 * 
 */
package com.synectiks.demo.site.repositories;

import org.springframework.stereotype.Repository;

import com.synectiks.commons.constants.IDBConsts;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.schemas.repositories.DynamoDbRepository;

/**
 * @author Rajesh
 */
@Repository
public class CustomerRepository extends DynamoDbRepository<Customer, String> {

	public CustomerRepository() {
		super(Customer.class);
	}

	/**
	 * Find a customer by username
	 * @param username
	 * @return
	 */
	public Customer findByUsername(String username) {
		Iterable<Customer> users = super.findByKeyValue(IDBConsts.Col_USERNAME, username);
		if (!IUtils.isNull(users) && !IUtils.isNull(users.iterator())
				&& users.iterator().hasNext()) {
			return users.iterator().next();
		}
		return null;
	}

	/**
	 * Find a customer by username
	 * @param username
	 * @return
	 */
	public Customer findByEmail(String email) {
		Iterable<Customer> users = super.findByKeyValue(IDBConsts.Col_EMAIL, email);
		if (!IUtils.isNull(users) && !IUtils.isNull(users.iterator())
				&& users.iterator().hasNext()) {
			return users.iterator().next();
		}
		return null;
	}

}
