package com.synectiks.demo.site.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.CommonDaoClass;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.OrderRepository;

/**
 * @author Rajesh
 */
@Controller
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private CartRepository cartRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private CommonDaoClass daoClass;

	@RequestMapping("/order/{cartId}")
	public String createOrder(@PathVariable("cartId") String cartId) {
		logger.info("daoClass: " + daoClass);
		logger.info("CartId: " + cartId);
		Cart cart = cartRepo.findById(cartId);
		if (!IUtils.isNull(cart)) {
			CustomerOrder customerOrder = new CustomerOrder();
			customerOrder.setCartId(cartId);

			Customer customer = custRepo.findById(cart.getCustomerId());
			customerOrder.setCustomerId(customer.getId());
			customerOrder.setBillingId(customer.getBillingId());
			customer.setShippingId(customer.getShippingId());

			orderRepo.save(customerOrder);
		} else {
			logger.info("Invalid cart id: " + cartId);
		}

		return "redirect:/checkout?id=" + cartId;
	}

}
