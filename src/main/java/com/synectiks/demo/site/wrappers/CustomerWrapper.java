/**
 * 
 */
package com.synectiks.demo.site.wrappers;

import java.io.Serializable;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.ShippingAddress;

/**
 * @author Rajesh
 */
public class CustomerWrapper implements Serializable {

	private static final long serialVersionUID = -5965089146648718971L;

	private Customer customer;
	private BillingAddress billing;
	private ShippingAddress shipping;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BillingAddress getBilling() {
		return billing;
	}

	public void setBilling(BillingAddress billing) {
		this.billing = billing;
	}

	public ShippingAddress getShipping() {
		return shipping;
	}

	public void setShipping(ShippingAddress shipping) {
		this.shipping = shipping;
	}

}
