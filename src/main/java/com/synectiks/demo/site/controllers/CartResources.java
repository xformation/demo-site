package com.synectiks.demo.site.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.CartItem;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;

import java.util.List;

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
	public @ResponseBody Cart getCartById(@PathVariable(value = "cartId") String cartId) {
		return cartRepo.findById(cartId);
	}

	@RequestMapping(value = "/add/{productId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void addItem(@PathVariable(value = "productId") String productId,
			String curUser) {
		Customer customer = custRepo.findByUsername(curUser);
		Cart cart = cartRepo.findById(customer.getCartId());
		Product product = productRepo.findById(productId);
		List<String> cartItems = cart.getCartItems();

		for (String key : cartItems) {
			CartItem item = cartItemRepo.findById(key);
			if (!IUtils.isNullOrEmpty(productId)
					&& productId.equals(item.getProductId())) {
				item.setQuantity(item.getQuantity() + 1);
				item.setTotalPrice(product.getPrice() * item.getQuantity());
				cartItemRepo.save(item);
				return;
			}
		}

		CartItem item = new CartItem();
		item.setProductId(product.getId());
		item.setQuantity(1);
		item.setTotalPrice(product.getPrice() * item.getQuantity());
		cartItemRepo.save(item);
	}

	@RequestMapping(value = "/remove/{productId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable(value = "productId") String productId) {
		CartItem cartItem = cartItemRepo.findByProductId(productId);
		cartItemRepo.delete(cartItem);
	}

	@RequestMapping(value = "/{cartId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void clearCart(@PathVariable(value = "cartId") String cartId) {
		Cart cart = cartRepo.findById(cartId);
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
