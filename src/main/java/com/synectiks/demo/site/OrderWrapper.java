/**
 * 
 */
package com.synectiks.demo.site;

import java.io.Serializable;

import com.synectiks.demo.site.dto.OrderDTO;

/**
 * @author Rajesh
 */
public class OrderWrapper implements Serializable {

	private static final long serialVersionUID = -6442740891638781352L;

	private OrderDTO order;

	public OrderDTO getOrder() {
		return order;
	}

	public void setOrder(OrderDTO order) {
		this.order = order;
	}

}
