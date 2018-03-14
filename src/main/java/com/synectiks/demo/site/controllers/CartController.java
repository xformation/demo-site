package com.synectiks.demo.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.demo.site.repositories.CustomerRepository;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/customer/cart")
public class CartController {

	@Autowired
	private CustomerRepository custRepo;

	@RequestMapping
	public String getCart(String username) {
		Customer customer = custRepo.findByUsername(username);
		return "redirect:/customer/cart/" + customer.getCartId();
	}

	@RequestMapping("/{id}")
	public String getCartRedirect(@PathVariable(value = "id") String id, Model model) {
		model.addAttribute(id);
		return "shoppingCart";
	}

}
