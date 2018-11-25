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
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.CartItem;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.Product;
import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.entities.oak.OakFileNode;
import com.synectiks.commons.exceptions.SynectiksException;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.BillingDTO;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CartItemDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.OrderDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.dto.ShippingDTO;
import com.synectiks.demo.site.repositories.BillingRepository;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.repositories.ShippingRepository;

/**
 * @author Rajesh
 */
public interface IDemoUtils {

	Logger logger = LoggerFactory.getLogger(IDemoUtils.class);

	String APP_SSMID = "DemoSite";

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
				if (!IUtils.isNull(item)) {
					setProductInCartItem(item, productRepo);
					cart.addAnItem(item);
				}
			}
		}
	}

	/**
	 * Method to set product into cart item
	 * @param item
	 * @param productRepo
	 */
	static void setProductInCartItem(CartItemDTO item, ProductRepository productRepo) {
		if (!IUtils.isNull(item) && !IUtils.isNullOrEmpty(item.getProductId())) {
			ProductDTO prod = IDemoUtils.wrapInDTO(
					productRepo.findById(item.getProductId()), ProductDTO.class);
			logger.info("Prod: " + prod);
			if (!IUtils.isNull(prod)) {
				item.setProduct(prod);
			}
		}
	}

	/**
	 * Method to remove (update status as removed) the finished cart from db.
	 * @param cartRepo
	 * @param cartItemRepo
	 * @param productRepo
	 * @param custRepo
	 * @param billRepo
	 * @param shipRepo
	 * @param cartId
	 */
	static void clearOrder(CartRepository cartRepo, CartItemRepository cartItemRepo,
			ProductRepository productRepo, CustomerRepository custRepo,
			BillingRepository billRepo, ShippingRepository shipRepo, String cartId) {
		if (IUtils.isNull(cartId)) {
			logger.info("Invalid cart id: " + cartId);
		} else {
			Cart cart = cartRepo.findById(cartId).orElse(null);
			if (!IUtils.isNull(cart)) {
				for (String itemId : cart.getCartItems()) {
					CartItem cItem = cartItemRepo.findById(itemId).orElse(null);
					if (!IUtils.isNull(cItem)) {
						if (!IUtils.isNullOrEmpty(cItem.getProductId())) {
							Product prod = productRepo.findById(cItem.getProductId()).orElse(null);
							// Update items quantity
							prod.setStockCount(prod.getStockCount() - cItem.getQuantity());
							productRepo.save(prod);
						}
						cItem.setState("SOLD");
						cartItemRepo.save(cItem);
					}
				}
				Customer cust = custRepo.findById(cart.getCustomerId()).orElse(null);
				cust.setCartId(null);
				custRepo.save(cust);
			}
		}
	}

	/**
	 * Load an order by cartid
	 * @param cartId
	 * @return
	 */
	static OrderDTO getOrder(CartRepository cartRepo, CartItemRepository cartItemRepo,
			ProductRepository productRepo, CustomerRepository custRepo,
			BillingRepository billRepo, ShippingRepository shipRepo, String cartId) {
		OrderDTO order = null;
		if (IUtils.isNull(cartId)) {
			logger.info("Invalid cart id: " + cartId);
		} else {
			try {
				order = new OrderDTO();
				// Set cart
				CartDTO cart = IDemoUtils.wrapInDTO(
						cartRepo.findById(cartId), CartDTO.class);
				fillCartDto(cart, cartItemRepo, productRepo);
				order.setCartId(cart.getId());
				order.setCart(cart);
				// Set customer
				CustomerDTO customer = getCustomer(custRepo, cart);
				logger.info("Found customer: " + customer);
				order.setCustomerId(customer.getId());
				order.setCustomer(customer);
				// Set Billing
				BillingDTO billing = getBilling(custRepo, billRepo, customer);
				order.setBillingId(billing.getId());
				order.setBilling(billing);
				// Set Shipping
				ShippingDTO shipping = getShipping(custRepo, shipRepo, customer);
				order.setShippingId(shipping.getId());
				order.setShipping(shipping);
				logger.info("Order object is filled");
			} catch (SynectiksException se) {
				logger.error("Failed to collect order info", se);
			}
		}
		return order;
	}

	/**
	 * Method to fetch customer from cart object.
	 * @param custRepo 
	 * @param cart
	 * @return
	 * @throws SynectiksException
	 */
	static CustomerDTO getCustomer(CustomerRepository custRepo, CartDTO cart)
			throws SynectiksException {
		if (IUtils.isNull(cart) || IUtils.isNullOrEmpty(cart.getCustomerId())) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		if (IUtils.isNull(custRepo)) {
			logger.info("Customer repository is null");
		}
		return IDemoUtils.wrapInDTO(
				custRepo.findById(cart.getCustomerId()), CustomerDTO.class);
	}

	/**
	 * Method to fetch or create shipping object for customer.
	 * @param custRepo 
	 * @param shipRepo 
	 * @param customer
	 * @return
	 * @throws SynectiksException
	 */
	static ShippingDTO getShipping(CustomerRepository custRepo, ShippingRepository shipRepo,
			CustomerDTO customer) throws SynectiksException {
		if (IUtils.isNull(customer)) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		logger.info("Fetching shipping address: " + customer.getShippingId());
		if (IUtils.isNull(shipRepo)) {
			logger.info("Shipping repository is null");
		}
		ShippingDTO shipping = null;
		if (!IUtils.isNullOrEmpty(customer.getShippingId())) {
			shipping = IDemoUtils.wrapInDTO(
					shipRepo.findById(customer.getShippingId()), ShippingDTO.class);
			logger.info("Found shipping: " + shipping);
		} else {
			shipping = IDemoUtils.wrapInDTO(
					shipRepo.save(new ShippingAddress()), ShippingDTO.class);
			customer.setShippingId(shipping.getId());
			custRepo.save(IDemoUtils.wrapInDTO(customer, Customer.class));
			logger.info("New shipping created: " + shipping.getId());
		}
		return shipping;
	}

	/**
	 * Method to fetch or create shipping object for customer.
	 * @param custRepo 
	 * @param billRepo 
	 * @param customer
	 * @return
	 * @throws SynectiksException
	 */
	static BillingDTO getBilling(CustomerRepository custRepo, BillingRepository billRepo,
			CustomerDTO customer) throws SynectiksException {
		if (IUtils.isNull(customer)) {
			throw new SynectiksException("Invalid/Null cart object.");
		}
		logger.info("Fetching billing address: " + customer.getShippingId());
		if (IUtils.isNull(billRepo)) {
			logger.info("Billing repository is null");
		}
		BillingDTO billing = null;
		if (!IUtils.isNullOrEmpty(customer.getBillingId())) {
			billing = IDemoUtils.wrapInDTO(
					billRepo.findById(customer.getBillingId()), BillingDTO.class);
			logger.info("Found billing: " + billing);
		} else {
			billing = IDemoUtils.wrapInDTO(
					billRepo.save(new BillingAddress()), BillingDTO.class);
			customer.setBillingId(billing.getId());
			custRepo.save(IDemoUtils.wrapInDTO(customer, Customer.class));
			logger.info("new billing created: " + billing.getId());
		}
		return billing;
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

	/**
	 * Method to get machine created on state machine
	 * @param username
	 * @param cartId
	 * @return
	 */
	static String createStateMachine(String username, String cartId) {
		String ssmId = APP_SSMID + ":" + username + ":" + cartId;
		/*Map<String, Object> params = new HashMap<>();
		params.put(IConsts.PRM_MACHINE_ID, ssmId);
		try {
			return IUtils.getStringFromPostReq(url, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}*/
		return ssmId;
	}

	static void sendSsmEvent(String url, String ssmId, String event) {
		Map<String, Object> params = new HashMap<>();
		params.put(IConsts.PRM_MACHINE_ID, ssmId);
		params.put(IConsts.PRM_EVENT, event);
		try {
			IUtils.getStringFromPostReq(url, params);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
