package com.synectiks.demo.site.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.webflow.context.ExternalContext;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.commons.exceptions.SynectiksException;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.OrderDTO;
import com.synectiks.demo.site.repositories.BillingRepository;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.OrderRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.repositories.ShippingRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private CartRepository cartRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private BillingRepository billRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private ShippingRepository shipRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private CartItemRepository cartItemRepo;

	@RequestMapping("/order/{cartId}")
	public String createOrder(@PathVariable("cartId") String cartId,
			HttpServletRequest request) {
		logger.info("CartId: " + cartId);
		Cart cart = cartRepo.findById(cartId);
		if (IUtils.isNull(cart)) {
			logger.info("Invalid cart id: " + cartId);
		} else {
			CustomerOrder customerOrder = new CustomerOrder();
			customerOrder.setCartId(cartId);

			Customer customer = custRepo.findById(cart.getCustomerId());
			customerOrder.setCustomerId(customer.getId());
			customerOrder.setBillingId(customer.getBillingId());
			customerOrder.setShippingId(customer.getShippingId());

			orderRepo.save(customerOrder);
			// set order into session
			request.getSession().setAttribute(
					OrderDTO.ORDER_SESSION_KEY, IDemoUtils.getOrder(cartRepo, cartItemRepo,
							productRepo, custRepo, billRepo, shipRepo, cartId));
		}
		return "redirect:/checkout?id=" + cartId;
	}

	/**
	 * Method to get order from session for order flow.
	 * @param cartId
	 * @return
	 * @throws SynectiksException
	 */
	public static OrderDTO getOrder(ExternalContext context, String cartId)
			throws SynectiksException {
		OrderDTO order = (OrderDTO) context.getSessionMap().get(OrderDTO.ORDER_SESSION_KEY);
		logger.info("Found order: " + order);
		if (IUtils.isNull(order) || (!IUtils.isNullOrEmpty(order.getCartId()) &&
				!order.getCartId().equals(cartId))) {
			logger.error("Invalid cart id: " + cartId);
			throw new SynectiksException("Invalid cart id: " + cartId);
		}
		return order;
	}

	public static void clearCart(ExternalContext context, String cartId)
			throws SynectiksException {
		if (IUtils.isNullOrEmpty(cartId)) {
			logger.error("Invalid cart id: " + cartId);
			throw new SynectiksException("Invalid cart id: " + cartId);
		}
		// Remove attribute from session
		context.getSessionMap().remove(OrderDTO.ORDER_SESSION_KEY);
	}
}
