package com.synectiks.demo.site.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.synectiks.commons.utils.IUtils;
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
@SessionAttributes("curCustomer")
public class AdminHomeController {

	@Autowired
	private ProductRepository prodRepo;
	@Autowired
	private CustomerRepository custRepo;

	@RequestMapping
	public String adminHomePage(HttpServletRequest request) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		return "admin";
	}

	@RequestMapping("/inventory")
	public String inventory(HttpServletRequest request, Model model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		List<ProductDTO> productList = IDemoUtils.wrapIterableInDTOList(
				prodRepo.findAll(), ProductDTO.class);
		model.addAttribute("productList", productList);
		return "productInventory";
	}

	@RequestMapping("/customer")
	public String customerManagement(HttpServletRequest request, Model model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		List<CustomerDTO> customerList = IDemoUtils.wrapIterableInDTOList(
				custRepo.findAll(), CustomerDTO.class);
		model.addAttribute("customerList", customerList);
		return "adminListUsers";
	}

}
