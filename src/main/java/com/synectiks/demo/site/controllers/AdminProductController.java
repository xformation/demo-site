package com.synectiks.demo.site.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	private String ctxPath;
	private String rootPath;

	@Autowired
	private RestTemplate rest;
	@Autowired
	private ProductRepository prodRepo;
	@Value("${" + IDemoUtils.JCR_BASE_URL + "}")
	private String baseUrl;
	@Value("${" + IDemoUtils.JCR_CREATE_NODE + "}")
	private String createNode;
	@Value("${" + IDemoUtils.JCR_REMOVE_NODE + "}")
	private String removeNode;
	@Value("${" + IDemoUtils.JCR_UPLOAD_FILE + "}")
	private String uploadFile;

	@RequestMapping("/inventory/add")
	public ModelAndView addProduct(HttpServletRequest request, ModelMap model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return new ModelAndView("login");
		}
		ctxPath = request.getContextPath();
		rootPath = request.getSession().getServletContext().getRealPath("/");
		ProductDTO product = new ProductDTO();
		product.setCondition("New");
		model.addAttribute("categoryList", categories);
		model.addAttribute("productDTO", product);

		return new ModelAndView("addProduct");
	}

	@RequestMapping(value = "/inventory/add", method = RequestMethod.POST)
	public String addProductPost(@ModelAttribute("productDTO") ProductDTO dto,
			BindingResult result, ModelMap model) {
		logger.info("Obj: " + dto + "\n" + model);
		model.addAttribute("categoryList", categories);
		if (result.hasErrors()) {
			return "redirect:/admin/inventory/add";
		}
		if (!IUtils.isNull(dto)) {
			if (IUtils.isNullOrEmpty(dto.getName()))
				result.addError(new FieldError("productDTO", "name",
						"Product name is null."));
			if (IUtils.isNullOrEmpty(dto.getManufacturer()))
				result.addError(new FieldError("productDTO", "manufacturer",
						"Product manufacturer is null."));
			if (dto.getPrice() <= 0)
				result.addError(new FieldError("productDTO", "price",
						"Product price must be > 0"));

			if (result.hasErrors()) {
				return "redirect:/admin/inventory/add";
			}
		}
		MultipartFile image = dto.getImage();
		Product entity = IDemoUtils.createCopyProperties(dto, Product.class);
		entity = prodRepo.save(entity);
		dto = IDemoUtils.createCopyProperties(entity, ProductDTO.class);
		logger.info("Product: " + dto.getId());
		path = Paths.get(rootPath + IDemoUtils.RES_PROD_IMG_PATH
				+ dto.getId() + ".png");
		logger.info("image path: " + path);
		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(path.toFile());
				// Save image into jcr repository
				String nodePath = String.format(IDemoUtils.JCR_IMAGE_PATH,
						ctxPath, dto.getCategory());
				nodePath = IDemoUtils.removeNonAlphaNumericChars(nodePath);
				String upFilePath = IDemoUtils.uploadFile(IDemoUtils.getApiUrl(
						baseUrl, uploadFile), path.toFile(), nodePath);
				upFilePath = upFilePath.replaceAll("\\\\", "/");
				IDemoUtils.createFileNode(IDemoUtils.getApiUrl(
						baseUrl, createNode), rest, path, nodePath, upFilePath);
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
		ctxPath = request.getContextPath();
		rootPath = request.getSession().getServletContext().getRealPath("/");
		ProductDTO productDTO = IDemoUtils.wrapInDTO(
				prodRepo.findById(id), ProductDTO.class);
		model.addAttribute("categoryList", categories);
		model.addAttribute("product", productDTO);

		return "editProduct";
	}

	@RequestMapping(value = "/inventory/edit", method = RequestMethod.POST)
	public String editProductPost(@ModelAttribute("productDTO") ProductDTO dto,
			BindingResult result, Model model) throws RuntimeException {
		model.addAttribute("categoryList", categories);
		if (result.hasErrors()) {
			return "editProduct";
		}
		MultipartFile image = dto.getImage();
		path = Paths.get(rootPath + IDemoUtils.RES_PROD_IMG_PATH
				+ dto.getId() + ".png");

		if (image != null && !image.isEmpty()) {
			try {
				image.transferTo(path.toFile());
				// Save image into jcr repository
				String nodePath = String.format(
						IDemoUtils.JCR_IMAGE_PATH, ctxPath, dto.getCategory());
				nodePath = IDemoUtils.removeNonAlphaNumericChars(nodePath);
				logger.info("NodePath: " + nodePath);
				String upFilePath = IDemoUtils.uploadFile(IDemoUtils.getApiUrl(
						baseUrl, uploadFile), path.toFile(), nodePath);
				upFilePath = upFilePath.replaceAll("\\\\", "/");
				logger.info("uploaded file path: " + upFilePath);
				IDemoUtils.createFileNode(IDemoUtils.getApiUrl(
						baseUrl, createNode), rest, path, nodePath, upFilePath);
			} catch (Exception e) {
				throw new RuntimeException("The product image could not be saved.", e);
			}
		}
		prodRepo.save(IDemoUtils.createCopyProperties(dto, Product.class));

		return "redirect:/admin/inventory";
	}

	@RequestMapping("/inventory/remove/{id}")
	public String deleteProduct(@PathVariable("id") String id, Model model,
			HttpServletRequest request) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		if (IUtils.isNull(customer)) {
			return "redirect:/login";
		}
		ctxPath = request.getContextPath();
		rootPath = request.getSession().getServletContext().getRealPath("/");
		path = Paths.get(rootPath + IDemoUtils.RES_PROD_IMG_PATH + id + ".png");
		if (Files.exists(path)) {
			try {
				Files.delete(path);
				Product prod = prodRepo.findById(id).orElse(null);
				// Save image into jcr repository
				String nodePath = String.format(
						IDemoUtils.JCR_IMAGE_PATH, prod.getCategory());
				nodePath = IDemoUtils.removeNonAlphaNumericChars(nodePath);
				IDemoUtils.removeFileNode(IDemoUtils.getApiUrl(
						baseUrl, removeNode), rest, path, nodePath);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		prodRepo.delete(id);

		return "redirect:/admin/inventory";
	}

}
