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
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CartItemDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

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
	public @ResponseBody CartDTO getCartById(@PathVariable(value = "cartId") String cartId) {
		logger.info("Cart: " + cartId);
		CartDTO cart = null;
		if (!IUtils.isNullOrEmpty(cartId)) {
			cart = IDemoUtils.wrapInDTO(cartRepo.findById(cartId), CartDTO.class);
		}
		if (!IUtils.isNull(cart) && !IUtils.isNull(cart.getCartItems())) {
			for (String itemId : cart.getCartItems()) {
				CartItemDTO item = IDemoUtils.wrapInDTO(cartItemRepo.findById(
						itemId), CartItemDTO.class);
				logger.info(itemId + ": " + item);
				if (!IUtils.isNull(item)) {
					ProductDTO prod = IDemoUtils.wrapInDTO(
							productRepo.findById(item.getProductId()), ProductDTO.class);
					logger.info("Prod: " + prod);
					if (!IUtils.isNull(prod)) {
						item.setProduct(prod);
					}
				}
				cart.addAnItem(item);
			}
		}
		logger.info("Returning: " + cart);
		return cart;
	}

	@RequestMapping(value = "/add/{curUser}/{productId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void addItem(@PathVariable(value = "curUser") String curUser,
			@PathVariable(value = "productId") String productId) {
		Customer customer = custRepo.findById(curUser);
		if (IUtils.isNull(customer)) {
			logger.error("Customer details are missing.");
			return;
		}
		Cart cart = null;
		if (IUtils.isNullOrEmpty(customer.getCartId())) {
			cart = cartRepo.save(new Cart());
			customer.setCartId(cart.getId());
			custRepo.save(IDemoUtils.createCopyProperties(customer, Customer.class));
		} else {
			cart = cartRepo.findById(customer.getCartId());
		}
		Product product = productRepo.findById(productId);
		List<String> cartItems = cart.getCartItems();
		if (!IUtils.isNull(cartItems)) {
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
		}
		CartItem item = new CartItem();
		item.setProductId(product.getId());
		item.setQuantity(1);
		item.setTotalPrice(product.getPrice() * item.getQuantity());
		item = cartItemRepo.save(item);
		cart.addAnItem(item);
		cartRepo.save(cart);
	}

	@RequestMapping(value = "/remove/{cartId}/{cartItemId}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeItem(@PathVariable(value = "cartId") String cartId,
			@PathVariable(value = "cartItemId") String cartItemId) {
		// delete from repository
		cartItemRepo.delete(cartItemId);
		Cart cart = cartRepo.findById(cartId);
		if (!IUtils.isNull(cart) && !IUtils.isNull(cart.getCartItems())) {
			cart.getCartItems().remove(cartItemId);
		}
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
