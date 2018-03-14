package com.synectiks.demo.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.demo.site.repositories.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/admin")
public class AdminProductController {

	private static Map< String, String > categories = new LinkedHashMap<>();

	static {
		categories.put("Other", "Other...");
        categories.put("School Utilies", "School Utilies");
        categories.put("Books", "Books");
        categories.put("Accessories", "Accessories");
        categories.put("Sound systems", "Sound systems");
        categories.put("Musical instruments", "Instrumentos Musicales");
        categories.put("Computer / Electronics", "Computer / Electronics");
        categories.put("Mobile / Tablets", "Mobile / Tablets");
        categories.put("Home", "Home");
        categories.put("Furniture", "Furniture");
	}

	private Path path;
	@Autowired
	private ProductRepository prodRepo;

	@RequestMapping("/inventory/add")
	public String addProduct(Model model) {
		Product product = new Product();
		product.setCondition("New");
		model.addAttribute("categoryList", categories);
		model.addAttribute(product);

		return "addProduct";
	}

	@RequestMapping(value = "/inventory/add", method = RequestMethod.POST)
	public String addProductPost(@ModelAttribute("product") Product product,
			BindingResult result, HttpServletRequest request, Model model) {
		model.addAttribute("categoryList", categories);

		if (result.hasErrors()) {
			return "addProduct";
		}
		prodRepo.save(product);
		MultipartFile image = product.getImage();
		String root_directory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(root_directory + "\\WEB-INF\\resources\\product_images\\"
				+ product.getId() + ".png");

		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(new File(path.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("The product image could not be saved.", e);
			}
		}
		return "redirect:/admin/inventory";
	}

	@RequestMapping("/inventory/edit/{productId}")
	public String editProduct(@PathVariable("productId") String id, Model model) {
		Product product = prodRepo.findById(id);
		model.addAttribute("categoryList", categories);
		model.addAttribute("product", product);

		return "editProduct";
	}

	@RequestMapping(value = "/inventory/edit", method = RequestMethod.POST)
	public String editProductPost(@ModelAttribute("product") Product product,
			BindingResult result, Model model, HttpServletRequest request)
			throws RuntimeException {
		model.addAttribute("categoryList", categories);
		if (result.hasErrors()) {
			return "editProduct";
		}
		MultipartFile image = product.getImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootDirectory + "\\WEB-INF\\resources\\product_images\\"
				+ product.getId() + ".png");

		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(new File(path.toString()));
			} catch (Exception e) {
				throw new RuntimeException(
						"La imagen del product no pudo ser guardada.\n" + e);
			}
		}
		prodRepo.save(product);

		return "redirect:/admin/inventory";
	}

	@RequestMapping("/inventory/remove/{productId}")
	public String deleteProduct(@PathVariable("productId") String id, Model model,
			HttpServletRequest request) {
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootDirectory + "\\WEB-INF\\resources\\product_images\\"
				+ id + ".png");
		if (Files.exists(path)) {
			try {
				Files.delete(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		prodRepo.delete(id);

		return "redirect:/admin/inventory";
	}

}
