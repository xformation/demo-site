package com.synectiks.demo.site.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.Customer;
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
@RequestMapping("/customer/cart")
@SessionAttributes("curCustomer")
public class CartController {

	@Autowired
	private CartItemRepository cartItemRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private CartRepository cartRepo;

	@RequestMapping
	public String getCart(HttpServletRequest request) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
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
		return "redirect:/customer/cart/" + cart.getId();
	}

	private Cart getNewCart(CustomerDTO customer) {
		Cart cart = new Cart();
		cart.setCustomerId(customer.getId());
		cart = cartRepo.save(cart);
		customer.setCartId(cart.getId());
		custRepo.save(IDemoUtils.createCopyProperties(customer, Customer.class));
		return cart;
	}

	@RequestMapping("/{id}")
	public String getCartRedirect(HttpServletRequest request,
			@PathVariable(value = "id") String id, Model model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		CartDTO cartDTO = IDemoUtils.wrapInDTO(cartRepo.findById(id), CartDTO.class);
		IDemoUtils.fillCartDto(cartDTO, cartItemRepo, prodRepo);
		model.addAttribute("cartDTO", cartDTO);
		return "shoppingCart";
	}

}
