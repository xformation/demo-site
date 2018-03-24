package com.synectiks.demo.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

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
		List<ProductDTO> productList = IDemoUtils.wrapIterableInDTOList(
				prodRepo.findAll(), ProductDTO.class);
		model.addAttribute("productList", productList);
		return "productInventory";
	}

	@RequestMapping("/customer")
	public String customerManagement(Model model) {
		List<CustomerDTO> customerList = IDemoUtils.wrapIterableInDTOList(
				custRepo.findAll(), CustomerDTO.class);
		model.addAttribute("customerList", customerList);
		return "adminListUsers";
	}

}
