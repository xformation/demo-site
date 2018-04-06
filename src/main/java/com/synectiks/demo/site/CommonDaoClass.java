/**
 * 
 */
package com.synectiks.demo.site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.exceptions.SynectiksException;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.BillingDTO;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.ShippingDTO;
import com.synectiks.demo.site.repositories.BillingRepository;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.repositories.ShippingRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
public class CommonDaoClass {

	private static final Logger logger = LoggerFactory.getLogger(CommonDaoClass.class);

	@Autowired
	private CartRepository cartRepo;
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

	/**
	 * Method to validate the cartId and fetch a cart object.
	 * @param cartId
	 * @return
	 * @throws SynectiksException
	 */
	public CartDTO validateCart(String cartId) throws SynectiksException {
		if (IUtils.isNullOrEmpty(cartId)) {
			throw new SynectiksException("Null or empty cart id.");
		}
		Cart c = cartRepo.findById(cartId);
		CartDTO cart = IDemoUtils.wrapInDTO(c, CartDTO.class);
		IDemoUtils.fillCartDto(cart, cartItemRepo, productRepo);
		return cart;
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

}
