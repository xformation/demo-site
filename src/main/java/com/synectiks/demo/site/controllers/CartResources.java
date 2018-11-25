package com.synectiks.demo.site.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.CartItem;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/rest/cart")
public class CartResources {

	private static final Logger logger = LoggerFactory.getLogger(CartResources.class);

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private CartItemRepository cartItemRepo;

	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	private ProductRepository productRepo;

	@RequestMapping("/{cartId}")
	public @ResponseBody CartDTO getCartById(@PathVariable(value = "cartId") String cartId) {
		logger.info("Cart: " + cartId);
		CartDTO cart = null;
		if (!IUtils.isNullOrEmpty(cartId)) {
			cart = IDemoUtils.wrapInDTO(cartRepo.findById(cartId), CartDTO.class);
		}
		IDemoUtils.fillCartDto(cart, cartItemRepo, productRepo);
		logger.info("Returning: " + cart);
		return cart;
	}

	@RequestMapping(value = "/add/{curUser}/{productId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void addItem(@PathVariable(value = "curUser") String curUser,
			@PathVariable(value = "productId") String productId) {
		CustomerDTO customer = IDemoUtils.wrapInDTO(custRepo.findById(curUser),
				CustomerDTO.class);
		if (IUtils.isNull(customer)) {
			logger.error("Customer details are missing.");
			return;
		}
		Cart cart = null;
		if (IUtils.isNullOrEmpty(customer.getCartId())) {
			cart = getNewCart(customer);
		} else {
			cart = cartRepo.findById(customer.getCartId()).orElse(null);
			if (IUtils.isNull(cart)) {
				cart = getNewCart(customer);
			}
		}
		Product product = productRepo.findById(productId).orElse(null);
		List<String> cartItems = cart.getCartItems();
		if (!IUtils.isNull(cartItems)) {
			for (String key : cartItems) {
				CartItem item = cartItemRepo.findById(key).orElse(null);
				if (!IUtils.isNull(item) && !IUtils.isNullOrEmpty(productId)
						&& productId.equals(item.getProductId())) {
					item.setQuantity(item.getQuantity() + 1);
					item.setTotalPrice(product.getPrice() * item.getQuantity());
					cartItemRepo.save(item);
					return;
				}
			}
		}
		CartItem item = new CartItem();
		item.setProductId(product.getId());
		item.setQuantity(1);
		item.setTotalPrice(product.getPrice() * item.getQuantity());
		item = cartItemRepo.save(item);
		cart.addAnItem(item);
		cartRepo.save(cart);
	}

	private Cart getNewCart(CustomerDTO customer) {
		Cart cart = new Cart();
		cart.setCustomerId(customer.getId());
		cart = cartRepo.save(cart);
		customer.setCartId(cart.getId());
		custRepo.save(IDemoUtils.createCopyProperties(customer, Customer.class));
		return cart;
	}

	@RequestMapping(value = "/remove/{cartId}/{cartItemId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable(value = "cartId") String cartId,
			@PathVariable(value = "cartItemId") String cartItemId) {
		// delete from repository
		cartItemRepo.delete(cartItemId);
		Cart cart = cartRepo.findById(cartId).orElse(null);
		if (!IUtils.isNull(cart) && !IUtils.isNull(cart.getCartItems())) {
			cart.getCartItems().remove(cartItemId);
		}
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void clearCart(@PathVariable(value = "cartId") String cartId) {
		Cart cart = cartRepo.findById(cartId).orElse(null);
		for (String itemId : cart.getCartItems()) {
			cartItemRepo.delete(itemId);
		}
		cartRepo.delete(cartId);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST,
			reason = "Illegal request, please verify your payload")
	public void handleClientErrors(Exception ex) {
		logger.error(ex.getMessage(), ex);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR,
			reason = "Internal Server Error")
	public void handleServerErrors(Exception ex) {
		logger.error(ex.getMessage(), ex);
	}
}
