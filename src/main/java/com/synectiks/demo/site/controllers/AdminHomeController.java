package com.synectiks.demo.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.Product;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;

import java.util.List;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/admin")
public class AdminHomeController {

	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private CustomerRepository custRepo;

	@RequestMapping
	public String adminHomePage() {
		return "admin";
	}

	@RequestMapping("/inventory")
	public String inventario(Model model) {
		List<Product> productList = (List<Product>) prodRepo.findAll();
		model.addAttribute("productList", productList);
		return "productInventory";
	}

	@RequestMapping("/customer")
	public String customerManagement(Model model) {
		List<Customer> customerList = (List<Customer>) custRepo.findAll();
		model.addAttribute("customerList", customerList);
		return "adminListUsers";
	}

}
