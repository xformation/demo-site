package com.synectiks.demo.site.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private Environment env;
	@Autowired
	private RestTemplate rest;
	@Autowired
	private ProductRepository productRepo;

	@RequestMapping
	public String getProducts(Model model) {
		List<Product> productList = (List<Product>) productRepo.findAll();
		model.addAttribute("productList", productList);
		return "products";
	}

	@RequestMapping("/productDetails/{productId}")
	public String detallesProduct(@PathVariable String productId,
			HttpServletRequest request, Model model)
			throws IOException {
		Product product = productRepo.findById(productId);
		model.addAttribute(product);
		String path = request.getServletContext().getRealPath("/");
		path += "resources/product_images/";
		checkIfImageExists(path, product.getId(), product.getCategory());
		return "productDetails";
	}

	/**
	 * Method to check if file exits or create it by fetching from jcr path
	 * @param path
	 * @param name
	 * @param category 
	 */
	private void checkIfImageExists(String path, String name, String category) {
		String fileName = name + ".png";
		File img = new File(path, fileName );
		if (!IUtils.isNull(img) && !img.exists()) {
			String url = IDemoUtils.getApiUrl(env, IDemoUtils.JCR_LOAD_FILE);
			String nodePath = String.format(
					IDemoUtils.JCR_IMAGE_PATH, category) + fileName;
			IDemoUtils.saveImageFile(url, rest, nodePath, img);
		}
	}
}
