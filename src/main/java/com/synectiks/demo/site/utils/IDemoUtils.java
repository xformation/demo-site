/**
 * 
 */
package com.synectiks.demo.site.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.synectiks.commons.entities.oak.OakFileNode;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CartItemDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.ProductRepository;

/**
 * @author Rajesh
 */
public interface IDemoUtils {

	Logger logger = LoggerFactory.getLogger(IDemoUtils.class);

	String JCR_BASE_URL = "jcr.repo.server.url";
	String JCR_DOWNLOAD_FILE = "jcr.download.file";
	String JCR_LOAD_FILE = "jcr.load.file.path";
	String JCR_CREATE_NODE = "jcr.create.node.path";
	String JCR_REMOVE_NODE = "jcr.remove.node.path";
	String JCR_UPLOAD_FILE = "jcr.upload.file";

	String CUR_CUSTOMER = "curCustomer";
	String RES_PROD_IMG_PATH = "resources/product_images/";
	/** {context_path}/{product_category}/prodImage */
	String JCR_IMAGE_PATH = "%s/%s/prodImage";

	/**
	 * Method to validate and test if user is logged in.
	 * @param request
	 * @return
	 */
	static CustomerDTO validateUser(HttpServletRequest request) {
		CustomerDTO customer = (CustomerDTO) request.getSession()
				.getAttribute(IDemoUtils.CUR_CUSTOMER);
		return customer;
	}

	/**
	 * Method to load properties from source to cls object using
	 * {@code BeanUtils#copyProperties(Object, Object)}
	 * @param src
	 * @param cls
	 * @return
	 */
	static <T> T createCopyProperties(Object src, Class<T> cls) {
		T instance = null;
		if (!IUtils.isNull(src)) {
			try {
				instance = cls.newInstance();
				BeanUtils.copyProperties(src, instance);
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error("Failed to instancate class: " + cls.getName(), e);
			} catch (BeansException be) {
				logger.error("Failed to fill bean: " + cls.getName(), be);
			}
		}
		return instance;
	}

	/**
	 * Method to create a DTO objects list form entity result iterator.
	 * @param <DTO>
	 * @param iterable
	 * @param cls
	 * @return
	 */
	static <DTO> List<DTO> wrapIterableInDTOList(Iterable<?> iterable, Class<DTO> cls) {
		if (IUtils.isNull(iterable)) {
			return null;
		}
		List<DTO> dtos = new ArrayList<>();
		for (Object item : iterable) {
			dtos.add(createCopyProperties(item, cls));
		}
		return dtos;
	}

	/**
	 * Method to create a DTO object form entity.
	 * @param <DTO>
	 * @param src
	 * @param cls
	 * @return
	 */
	static <DTO> DTO wrapInDTO(Object src, Class<DTO> cls) {
		return createCopyProperties(src, cls);
	}

	/**
	 * Method to fill cart object with items and products.
	 * @param cart
	 * @param cartItemRepo
	 * @param productRepo
	 */
	static void fillCartDto(CartDTO cart, CartItemRepository cartItemRepo,
			ProductRepository productRepo) {
		if (!IUtils.isNull(cart) && !IUtils.isNull(cart.getCartItems())) {
			for (String itemId : cart.getCartItems()) {
				CartItemDTO item = IDemoUtils.wrapInDTO(cartItemRepo.findById(itemId),
						CartItemDTO.class);
				logger.info(itemId + ": " + item);
				setProductInCartItem(item, productRepo);
				cart.addAnItem(item);
			}
		}
	}

	/**
	 * Method to set product into cart item
	 * @param item
	 * @param productRepo
	 */
	static void setProductInCartItem(CartItemDTO item, ProductRepository productRepo) {
		if (!IUtils.isNull(item)) {
			ProductDTO prod = IDemoUtils.wrapInDTO(
					productRepo.findById(item.getProductId()), ProductDTO.class);
			logger.info("Prod: " + prod);
			if (!IUtils.isNull(prod)) {
				item.setProduct(prod);
			}
		}
	}

	/**
	 * Method to create a oak file node with nodePath and file.
	 * @param url
	 * @param rest
	 * @param path
	 * @param nodePath
	 * @param upFilePath 
	 */
	static void createFileNode(String url, RestTemplate rest, Path path,
			String nodePath, String upFilePath) {
		if (IUtils.isNullOrEmpty(nodePath) || IUtils.isNull(path)) {
			logger.error("File or nodePath is null or empty.");
			return;
		}
		OakFileNode fileNode = OakFileNode.createNode(nodePath, path.toFile());
		fileNode.setPath(upFilePath);
		String res = IUtils.sendPostRestRequest(rest, url, null, String.class,
				IUtils.getParamMap(nodePath, fileNode, fileNode.getName()),
				MediaType.APPLICATION_FORM_URLENCODED);
		logger.info("Result: " + res);
	}

	/**
	 * Method to remove a node from jcr repository
	 * @param apiUrl
	 * @param rest
	 * @param path
	 * @param nodePath
	 */
	static void removeFileNode(String apiUrl, RestTemplate rest, Path path,
			String nodePath) {
		if (IUtils.isNullOrEmpty(nodePath) || IUtils.isNull(path)) {
			logger.error("File or nodePath is null or empty.");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("absPath", nodePath + "/" + path.getFileName());
		String res = IUtils.sendPostRestRequest(rest, apiUrl,
				 null, String.class, map, MediaType.APPLICATION_FORM_URLENCODED);
		logger.info("Result: " + res);
	}

	/**
	 * Method to save a node as file from jcr repository
	 * @param apiUrl
	 * @param rest
	 * @param nodePath
	 * @param img
	 */
	static void saveImage(String apiUrl, String nodePath, File img) {
		if (IUtils.isNullOrEmpty(nodePath)) {
			logger.error("File or nodePath is null or empty.");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("path", nodePath);
		try {
			InputStream is = IUtils.getStreamFromPostReq(apiUrl, map);
			logger.info("Result: " + is);
			if (!IUtils.isNull(is)) {
				FileUtils.copyInputStreamToFile(is, img);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Method to save a node string into file from jcr repository
	 * @param apiUrl
	 * @param rest
	 * @param nodePath
	 * @param img
	 */
	static void saveImageFile(String apiUrl, RestTemplate rest, String nodePath,
			File img) {
		if (IUtils.isNullOrEmpty(nodePath)) {
			logger.error("File or nodePath is null or empty.");
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("path", nodePath);
		String res = IUtils.sendPostRestRequest(rest, apiUrl, null, String.class, map,
				MediaType.APPLICATION_FORM_URLENCODED);
		logger.info("Result: " + res);
		if (!IUtils.isNullOrEmpty(res)) {
			try {
				//OakFileNode node = IUtils.OBJECT_MAPPER.readValue(res, OakFileNode.class);
				OakFileNode node = IUtils.createOakFileNode(res, img.getName());
				if (!IUtils.isNull(node) && !IUtils.isNull(node.getData())) {
					FileUtils.copyInputStreamToFile(node.getData(), img);
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Method to construct create node url.
	 * @param env
	 * @param apiKey
	 * @return
	 */
	static String getApiUrl(Environment env, String apiKey) {
		StringBuilder sb = new StringBuilder();
		String val = env.getProperty(JCR_BASE_URL);
		sb.append(IUtils.isNullOrEmpty(val) ? "" : val);
		val = env.getProperty(apiKey);
		sb.append(IUtils.isNullOrEmpty(val) ? "" : val);
		return sb.toString();
	}

	/**
	 * Method to construct create node url.
	 * @param baseUrl
	 * @param apiPath
	 * @return
	 */
	static String getApiUrl(String baseUrl, String apiPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(IUtils.isNullOrEmpty(baseUrl) ? "" : baseUrl);
		sb.append(IUtils.isNullOrEmpty(apiPath) ? "" : apiPath);
		IUtils.logger.info("Url: " + sb.toString());
		return sb.toString();
	}

	/**
	 * Method to upload file on jcr service server
	 * @param url 
	 * @param file
	 * @param nodePath 
	 * @return
	 */
	static String uploadFile(String url, File file, String nodePath) {
		if (IUtils.isNullOrEmpty(url) || (!IUtils.isNull(file) && !file.exists())) {
			logger.error("File or url is null or empty.");
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("upPath", nodePath);
		Map<String, File> files = new HashMap<>();
		files.put("file", file);
		String res = null;
		try {
			res = IUtils.sendMultiPartPostReq(url, map, files);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("Result: " + res);
		return res;
	}

	/**
	 * Method to remove all non numeric chars from node path
	 * @param input
	 * @return
	 */
	static String removeNonAlphaNumericChars(String input) {
		if (IUtils.isNullOrEmpty(input)) {
			return null;
		}
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char tmpChar = input.charAt(i);
			if (Character.isLetterOrDigit(tmpChar) ||
					tmpChar == '_' || tmpChar == '/' || tmpChar == '.') {
				result.append(tmpChar);
			}
		}
		return result.toString();
	}

}
