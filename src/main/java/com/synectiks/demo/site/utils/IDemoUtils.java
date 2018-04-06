/**
 * 
 */
package com.synectiks.demo.site.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;

import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.CartDTO;
import com.synectiks.demo.site.dto.CartItemDTO;
import com.synectiks.demo.site.dto.ProductDTO;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.ProductRepository;

/**
 * @author Rajesh
 */
public interface IDemoUtils {

	Logger logger = LoggerFactory.getLogger(IDemoUtils.class);

	String CUR_CUSTOMER = "curCustomer";

	/**
	 * Method to load properties from source to cls object
	 * using {@code BeanUtils#copyProperties(Object, Object)}
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
				CartItemDTO item = IDemoUtils.wrapInDTO(cartItemRepo.findById(
						itemId), CartItemDTO.class);
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
	
}
