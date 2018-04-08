package com.synectiks.demo.site.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.webflow.context.ExternalContext;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.exceptions.SynectiksException;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.BillingDTO;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.OrderDTO;
import com.synectiks.demo.site.dto.ShippingDTO;
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
			customer.setShippingId(customer.getShippingId());

			orderRepo.save(customerOrder);
			// set order into session
			request.getSession().setAttribute(
					OrderDTO.ORDER_SESSION_KEY, getOrder(cartId));
		}
		return "redirect:/checkout?id=" + cartId;
	}

	/**
	 * Load an order by cartid
	 * @param cartId
	 * @return
	 */
	public OrderDTO getOrder(String cartId) {
		OrderDTO order = null;
		if (IUtils.isNull(cartId)) {
			logger.info("Invalid cart id: " + cartId);
		} else {
			try {
				order = new OrderDTO();
				// Set cart
				CartDTO cart = IDemoUtils.wrapInDTO(
						cartRepo.findById(cartId), CartDTO.class);
				IDemoUtils.fillCartDto(cart, cartItemRepo, productRepo);
				order.setCartId(cart.getId());
				order.setCart(cart);
				// Set customer
				CustomerDTO customer = getCustomer(cart);
				order.setCustomerId(customer.getId());
				order.setCustomer(customer);
				// Set Billing
				BillingDTO billing = getBilling(customer);
				order.setBillingId(billing.getId());
				order.setBilling(billing);
				// Set Shipping
				ShippingDTO shipping = getShipping(customer);
				order.setShippingId(shipping.getId());
				order.setShipping(shipping);
				logger.info("Order object is filled");
			} catch (SynectiksException se) {
				logger.error("Failed to collect order info", se);
			}
		}
		return order;
	}

	/**
	 * Method to fetch customer from cart object.
	 * @param cart
	 * @return
	 * @throws SynectiksException
	 */
	public CustomerDTO getCustomer(CartDTO cart) throws SynectiksException {
		if (IUtils.isNull(cart) || IUtils.isNullOrEmpty(cart.getCustomerId())) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		if (IUtils.isNull(custRepo)) {
			logger.info("Customer repository is null");
		}
		return IDemoUtils.wrapInDTO(
				custRepo.findById(cart.getCustomerId()), CustomerDTO.class);
	}

	/**
	 * Method to fetch or create shipping object for customer.
	 * @param customer
	 * @return
	 * @throws SynectiksException
	 */
	public ShippingDTO getShipping(CustomerDTO customer) throws SynectiksException {
		if (IUtils.isNull(customer)) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		logger.info("Fetching shipping address: " + customer.getShippingId());
		if (IUtils.isNull(shipRepo)) {
			logger.info("Shipping repository is null");
		}
		ShippingDTO shipping = null;
		if (!IUtils.isNullOrEmpty(customer.getShippingId())) {
			shipping = IDemoUtils.wrapInDTO(
					shipRepo.findById(customer.getShippingId()), ShippingDTO.class);
		} else {
			shipping = IDemoUtils.wrapInDTO(
					shipRepo.save(new ShippingAddress()), ShippingDTO.class);
			customer.setShippingId(shipping.getId());
			custRepo.save(IDemoUtils.wrapInDTO(customer, Customer.class));
		}
		return shipping;
	}

	/**
	 * Method to fetch or create shipping object for customer.
	 * @param customer
	 * @return
	 * @throws SynectiksException
	 */
	public BillingDTO getBilling(CustomerDTO customer) throws SynectiksException {
		if (IUtils.isNull(customer)) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		logger.info("Fetching billing address: " + customer.getShippingId());
		if (IUtils.isNull(billRepo)) {
			logger.info("Billing repository is null");
		}
		BillingDTO billing = null;
		if (!IUtils.isNullOrEmpty(customer.getBillingId())) {
			billing = IDemoUtils.wrapInDTO(
					billRepo.findById(customer.getBillingId()), BillingDTO.class);
		} else {
			billing = IDemoUtils.wrapInDTO(
					billRepo.save(new BillingAddress()), BillingDTO.class);
			customer.setBillingId(billing.getId());
			custRepo.save(IDemoUtils.wrapInDTO(customer, Customer.class));
		}
		return billing;
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
