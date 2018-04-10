package com.synectiks.demo.site.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/admin")
@SessionAttributes("curCustomer")
public class AdminProductController {

	private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);
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
	private Environment env;
	@Autowired
	private RestTemplate rest;
	@Autowired
	private ProductRepository prodRepo;

	@RequestMapping("/inventory/add")
	public ModelAndView addProduct(HttpServletRequest request, ModelMap model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return new ModelAndView("login");
		}
		ProductDTO product = new ProductDTO();
		product.setCondition("New");
		model.addAttribute("categoryList", categories);
		model.addAttribute("productDTO", product);

		return new ModelAndView("addProduct");
	}

	@RequestMapping(value = "/inventory/add", method = RequestMethod.POST)
	public String addProductPost(@Valid @ModelAttribute("productDTO") ProductDTO productDTO,
			BindingResult result, HttpServletRequest request, ModelMap model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		logger.info("Obj: " + productDTO + "\n" + model);
		model.addAttribute("categoryList", categories);
		if (result.hasErrors()) {
			return "redirect:/admin/inventory/add";
		}
		if (!IUtils.isNull(productDTO)) {
			if (IUtils.isNullOrEmpty(productDTO.getName()))
				result.addError(new FieldError("productDTO", "name",
						"Product name is null."));
			if (IUtils.isNullOrEmpty(productDTO.getManufacturer()))
				result.addError(new FieldError("productDTO", "manufacturer",
						"Product manufacturer is null."));
			if (productDTO.getPrice() <= 0)
				result.addError(new FieldError("productDTO", "price",
						"Product price must be > 0"));

			if (result.hasErrors()) {
				return "redirect:/admin/inventory/add";
			}
		}
		MultipartFile image = productDTO.getImage();
		Product entity = IDemoUtils.createCopyProperties(productDTO, Product.class);
		entity = prodRepo.save(entity);
		productDTO = IDemoUtils.createCopyProperties(entity, ProductDTO.class);
		logger.info("Product: " + productDTO.getId());
		String root_directory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(root_directory + "/resources/product_images/"
				+ productDTO.getId() + ".png");
		logger.info("image path: " + path);
		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(new File(path.toString()));
				// Save image into jcr repository
				String nodePath = String.format(
						IDemoUtils.JCR_IMAGE_PATH, productDTO.getCategory());
				IDemoUtils.createFileNode(IDemoUtils.getApiUrl(
						env, IDemoUtils.JCR_CREATE_NODE), rest, path, nodePath);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("The product image could not be saved.", e);
			}
		}
		return "redirect:/admin/inventory";
	}

	@RequestMapping("/inventory/edit/{id}")
	public String editProduct(@PathVariable("id") String id,
			HttpServletRequest request, Model model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		ProductDTO productDTO = IDemoUtils.wrapInDTO(
				prodRepo.findById(id), ProductDTO.class);
		model.addAttribute("categoryList", categories);
		model.addAttribute("product", productDTO);

		return "editProduct";
	}

	@RequestMapping(value = "/inventory/edit", method = RequestMethod.POST)
	public String editProductPost(@ModelAttribute("productDTO") ProductDTO productDTO,
			BindingResult result, Model model, HttpServletRequest request)
			throws RuntimeException {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		model.addAttribute("categoryList", categories);
		if (result.hasErrors()) {
			return "editProduct";
		}
		MultipartFile image = productDTO.getImage();
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootDirectory + "/resources/product_images/"
				+ productDTO.getId() + ".png");

		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(new File(path.toString()));
				// Save image into jcr repository
				String nodePath = String.format(
						IDemoUtils.JCR_IMAGE_PATH, productDTO.getCategory());
				IDemoUtils.createFileNode(IDemoUtils.getApiUrl(
						env, IDemoUtils.JCR_CREATE_NODE), rest, path, nodePath);
			} catch (Exception e) {
				throw new RuntimeException("The product image could not be saved." + e);
			}
		}
		prodRepo.save(IDemoUtils.createCopyProperties(productDTO, Product.class));

		return "redirect:/admin/inventory";
	}

	@RequestMapping("/inventory/remove/{id}")
	public String deleteProduct(@PathVariable("id") String id, Model model,
			HttpServletRequest request) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		String rootDirectory = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootDirectory + "/resources/product_images/"
				+ id + ".png");
		if (Files.exists(path)) {
			try {
				Files.delete(path);
				Product prod = prodRepo.findById(id);
				// Save image into jcr repository
				String nodePath = String.format(
						IDemoUtils.JCR_IMAGE_PATH, prod.getCategory());
				IDemoUtils.removeFileNode(IDemoUtils.getApiUrl(
						env, IDemoUtils.JCR_REMOVE_NODE), rest, path, nodePath);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		prodRepo.delete(id);

		return "redirect:/admin/inventory";
	}

}
