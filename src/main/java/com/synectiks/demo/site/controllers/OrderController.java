package com.synectiks.demo.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.OrderRepository;

/**
 * @author Rajesh
 */
@Controller
public class OrderController {

	@Autowired
	private CartRepository cartRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private OrderRepository orderRepo;

	@RequestMapping("/order/{cartId}")
	public String createOrder(@PathVariable("cartId") String cartId) {
		CustomerOrder customerOrder = new CustomerOrder();
		Cart cart = cartRepo.findById(cartId);
		customerOrder.setCartId(cartId);

		Customer customer = custRepo.findById(cart.getCustomerId());
		customerOrder.setCustomerId(customer.getId());
		customerOrder.setBillingId(customer.getBillingId());
		customer.setShippingId(customer.getShippingId());

		orderRepo.save(customerOrder);

		return "redirect:/checkout?id=" + cartId;
	}

}
