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
		Customer customer = new Customer();
		BillingAddress billing = billingRepo.save(new BillingAddress());
		ShippingAddress shipping = shippingRepo.save(new ShippingAddress());

		customer.setBillingId(billing.getId());
		customer.setShippingId(shipping.getId());

		model.addAttribute("customer", customer);

		return "registerCustomer";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String registerPost(@ModelAttribute("customer") Customer customer,
			BindingResult result, Model model, String username) {
		if (result.hasErrors() || IUtils.isNull(customer)) {
			return "registerCustomer";
		}
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

		customer.setEnabled(true);
		customerRepo.save(customer);
		return "registerCustomerSuccess";
	}
}
