package com.synectiks.demo.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.synectiks.commons.entities.demo.BillingAddress;
import com.synectiks.commons.entities.demo.Customer;
import com.synectiks.commons.entities.demo.ShippingAddress;
import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.repositories.BillingRepository;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.repositories.ShippingRepository;
import com.synectiks.demo.site.wrappers.CustomerWrapper;

/**
 * @author Rajesh
 */
@Controller
public class RegisterController {

	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private BillingRepository billingRepo;
	@Autowired
	private ShippingRepository shippingRepo;

	@RequestMapping("/register")
	public String register(Model model) {
		CustomerWrapper wrapper = new CustomerWrapper();
		wrapper.setCustomer(new Customer());
		wrapper.setBilling(new BillingAddress());
		wrapper.setShipping(new ShippingAddress());

		model.addAttribute("wrapper", wrapper);

		return "registerCustomer";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("wrapper") CustomerWrapper wrapper,
			BindingResult result, Model model, String username) {
		if (result.hasErrors() || IUtils.isNull(wrapper)) {
			return "registerCustomer";
		}
		Customer customer = wrapper.getCustomer();
		// Check if email exists
		if (!IUtils.isNullOrEmpty(customer.getEmail())) {
			Customer cust = customerRepo.findByEmail(customer.getEmail());
			if (!IUtils.isNull(cust)) {
				model.addAttribute("email", "This email is already registered.");
				return "registerCustomer";
			}
		}
		// Check if username exists
		if (!IUtils.isNullOrEmpty(customer.getUsername())) {
			Customer cust = customerRepo.findByUsername(customer.getUsername());
			if (!IUtils.isNull(cust)) {
				model.addAttribute("username", "This username is already registered.");
				return "registerCustomer";
			}
		}
		BillingAddress billing = wrapper.getBilling();
		billing = billingRepo.save(billing);
		ShippingAddress shipping = wrapper.getShipping();
		shipping = shippingRepo.save(shipping);
		customer.setBillingId(billing.getId());
		customer.setShippingId(shipping.getId());
		customer.setEnabled(true);
		customer = customerRepo.save(customer);
		// set customer id in addresses
		shipping.setCustomerId(customer.getId());
		shippingRepo.save(shipping);
		billing.setCustomerId(customer.getId());
		billingRepo.save(billing);
		return "registerCustomerSuccess";
	}
}
