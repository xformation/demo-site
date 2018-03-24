package com.synectiks.demo.site.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.demo.site.repositories.ProductRepository;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepo;

	@RequestMapping
	public String getProducts(Model model) {
		List<Product> productList = (List<Product>) productRepo.findAll();
		model.addAttribute("productList", productList);
		return "products";
	}

	@RequestMapping("/productDetails/{productId}")
	public String detallesProduct(@PathVariable String productId, Model model)
			throws IOException {
		Product product = productRepo.findById(productId);
		model.addAttribute(product);

		return "productDetails";
	}
}
