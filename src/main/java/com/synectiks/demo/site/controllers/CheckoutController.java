package com.synectiks.demo.site.controllers;

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
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.synectiks.commons.constants.IConsts;
import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Cart;
import com.synectiks.commons.entities.demo.CustomerOrder;
import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.BillingDTO;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.OrderDTO;
import com.synectiks.demo.site.dto.ShippingDTO;
import com.synectiks.demo.site.repositories.BillingRepository;
import com.synectiks.demo.site.repositories.CartItemRepository;
import com.synectiks.demo.site.repositories.CartRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.OrderRepository;
import com.synectiks.demo.site.repositories.ProductRepository;
import com.synectiks.demo.site.repositories.ShippingRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
@RequestMapping("/shopping")
@SessionAttributes("curCustomer")
public class CheckoutController {

	private static final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

	@Value("${" + IConsts.STATE_URL_KEY + "}")
	private String baseUrl;
	@Autowired
	private CartRepository cartRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private BillingRepository billRepo;
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private ShippingRepository shipRepo;
	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private CartItemRepository cartItemRepo;

	@RequestMapping("/id/{cartId}")
	public String collectCustomerInfo(@PathVariable("cartId") String cartId,
			HttpServletRequest request, Model model) {
		CustomerDTO customer = IDemoUtils.validateUser(request);
		logger.info("CartId: " + cartId);
		Cart cart = cartRepo.findById(cartId).orElse(null);
		String retView = null;
		if (IUtils.isNull(cart)) {
			logger.info("Invalid cart id: " + cartId);
			retView = "checkout/invalidCartWarning";
		} else {
			OrderDTO order = IDemoUtils.getOrder(cartRepo, cartItemRepo,
					productRepo, custRepo, billRepo, shipRepo, cartId);
			CustomerOrder ordr = orderRepo.save(IDemoUtils.wrapInDTO(
					order, CustomerOrder.class));
			order.setId(ordr.getId());
			String ssmId = IDemoUtils.createStateMachine(customer.getUsername(), cartId);
			IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
					baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "Start");
			order.setSsmId(ssmId);
			model.addAttribute("orderDTO", order);
			retView = "checkout/collectCustomerInfo";
		}
		return retView;
	}

	@RequestMapping(path = "/saveBilling", params = {"Continue", "!Cancel"})
	public String saveBillingInfo(@ModelAttribute("orderDTO") OrderDTO order,
			BindingResult result, ModelMap model) {
		logger.info("Obj: " + order + "\n" + model);
		if (result.hasErrors()) {
			logger.error("Got errors: " + result);
			return "checkout/collectCustomerInfo";
		}
		String ssmId = order.getSsmId();
		if (!IUtils.isNull(order) && !IUtils.isNull(order.getBilling())) {
			logger.info("Filled obj: " + order.getBilling());
			if (IUtils.isNullOrEmpty(order.getBilling().getCardHolder()))
				result.addError(new FieldError("orderDTO", "billing.cardHolder",
						"Card holder is null."));
			if (IUtils.isNullOrEmpty(order.getBilling().getCardNumber()))
				result.addError(new FieldError("orderDTO", "billing.cardNumber",
						"CardNumber is null."));
			if (IUtils.isNullOrEmpty(order.getBilling().getSecurityCode()))
				result.addError(new FieldError("orderDTO", "billing.securityCode",
						"SecurityCode is null."));

			if (result.hasErrors()) {
				logger.error("Got errors: " + result);
				return "checkout/collectCustomerInfo";
			}
			order.getBilling().setId(order.getBillingId());
		}
		BillingAddress bill = billRepo.save(IDemoUtils.wrapInDTO(
				order.getBilling(), BillingAddress.class));
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "saveBilling");
		logger.info("Saved add: " + IDemoUtils.wrapInDTO(bill, BillingDTO.class));
		order = IDemoUtils.getOrder(cartRepo, cartItemRepo,
				productRepo, custRepo, billRepo, shipRepo, order.getCartId());
		order.setSsmId(ssmId);
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "collectShipping");
		model.addAttribute("orderDTO", order);
		return "checkout/collectShippingDetail";
	}

	@RequestMapping(path = "/saveBilling", params = {"Cancel", "!Continue"})
	public String cancelCustomerInfo(@RequestParam("ssmId") String ssmId) {
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "Cancel");
		return "checkout/checkOutCancelled";
	}

	@RequestMapping(path = "/saveShipping", params = {"Continue", "!Cancel"})
	public String saveShippingInfo(@ModelAttribute("orderDTO") OrderDTO order,
			BindingResult result, ModelMap model) {
		logger.info("Obj: " + order + "\n" + model);
		if (result.hasErrors()) {
			logger.error("Got errors: " + result);
			return "checkout/collectCustomerInfo";
		}
		String ssmId = order.getSsmId();
		if (!IUtils.isNull(order) && !IUtils.isNull(order.getShipping())) {
			if (IUtils.isNullOrEmpty(order.getShipping().getCity()))
				result.addError(new FieldError("orderDTO", "shipping.city",
						"City is null or empty."));
			if (IUtils.isNullOrEmpty(order.getShipping().getZip()))
				result.addError(new FieldError("orderDTO", "shipping.zip",
						"Zip is null or empty."));
			if (IUtils.isNullOrEmpty(order.getShipping().getState()))
				result.addError(new FieldError("orderDTO", "shipping.state",
						"State is null or empty."));

			if (result.hasErrors()) {
				logger.error("Got errors: " + result);
				return "checkout/collectCustomerInfo";
			}
			order.getShipping().setId(order.getShippingId());
		}
		ShippingAddress ship = shipRepo.save(IDemoUtils.wrapInDTO(
				order.getShipping(), ShippingAddress.class));
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "saveShipping");
		logger.info("Saved add: " + IDemoUtils.wrapInDTO(ship, ShippingDTO.class));
		order = IDemoUtils.getOrder(cartRepo, cartItemRepo,
				productRepo, custRepo, billRepo, shipRepo, order.getCartId());
		order.setSsmId(ssmId);
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "confirm");
		model.addAttribute("orderDTO", order);
		return "checkout/orderConfirmation";
	}

	@RequestMapping(path = "/saveShipping", params = {"Cancel", "!Continue"})
	public String cancelShippingInfo(@RequestParam("ssmId") String ssmId) {
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "saveBilling");
		return "checkout/checkOutCancelled";
	}

	@RequestMapping(path = "/confirm", params = {"Complete", "!Cancel"})
	public String confirm(@ModelAttribute("orderDTO") OrderDTO order,
			BindingResult result, ModelMap model) {
		logger.info("Obj: " + order + "\n" + model);
		String ssmId = order.getSsmId();
		try {
			IDemoUtils.clearOrder(cartRepo, cartItemRepo,
					productRepo, custRepo, billRepo, shipRepo, order.getCartId());
		} catch (Exception ex) {
			result.addError(new ObjectError(ex.getMessage(), ex.getMessage()));
			logger.error(ex.getMessage(), ex);
		}
		if (result.hasErrors()) {
			logger.error("Got errors: " + result);
			/*order = IDemoUtils.getOrder(cartRepo, cartItemRepo,
					productRepo, custRepo, billRepo, shipRepo, order.getCartId());*/
			model.addAttribute("orderDTO", order);
			return "checkout/orderConfirmation";
		}
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "complete");
		return "checkout/thanksCustomer";
	}

	@RequestMapping(path = "/saveShipping", params = {"Cancel", "!Complete"})
	public String cancelConfirm(@RequestParam("ssmId") String ssmId) {
		IDemoUtils.sendSsmEvent(IDemoUtils.getApiUrl(
				baseUrl, IConsts.API_MACHINE_EVENT), ssmId, "cancel");
		return "checkout/checkOutCancelled";
	}
}
