package com.synectiks.demo.site.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.synectiks.commons.utils.IUtils;
import com.synectiks.demo.site.dto.CustomerDTO;
import com.synectiks.demo.site.dto.Login;
import com.synectiks.demo.site.repositories.CustomerRepository;
import com.synectiks.demo.site.utils.IDemoUtils;

/**
 * @author Rajesh
 */
@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private CustomerRepository custRepo;

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			@RequestParam(value = "redirect", required = false) String redirect,
			Model model, HttpServletRequest request) {
		if (error != null) {
			model.addAttribute("error", "The username or password are invalid.");
		}
		if (logout != null) {
			request.getSession().removeAttribute(IDemoUtils.CUR_CUSTOMER);
			model.addAttribute("msg", "The session has ended successfully.");
		}
		if (!IUtils.isNullOrEmpty(redirect)) {
			model.addAttribute("redirect", redirect);
		}
		if (!model.asMap().containsKey("login")) {
			// Set empty form
			model.addAttribute("login", new Login());
		}
		return "login";
	}

	@RequestMapping("/signin")
	public String login(@Valid @ModelAttribute("login") Login form, Model model,
			HttpServletRequest request) {
		String error = null;
		String redirectUrl = form.getRedirect();
		String redirect = "redirect:/";
		logger.info("Form: " + form + "\nModel: " + model);
		CustomerDTO customer = IDemoUtils.wrapInDTO(
				custRepo.findByUsername(form.getUsername()), CustomerDTO.class);
		if (!IUtils.isNull(customer) && !IUtils.isNull(customer.getPassword())) {
			if (customer.getPassword().equals(form.getPassword())) {
				customer.setPassword(null);
				request.getSession().setAttribute(IDemoUtils.CUR_CUSTOMER, customer);
			} else {
				error = "Invalid password";
			}
		} else {
			error = "username doesn't exists";
		}
		// check if error return to home page or on redirect page
		if (!IUtils.isNullOrEmpty(error)) {
			redirect += "login?error=" + error;
			if (!IUtils.isNullOrEmpty(redirectUrl)) {
				redirect += "&redirect=" + redirectUrl;
			}
			model.addAttribute("login", form);
		} else if (!IUtils.isNullOrEmpty(redirectUrl)) {
			redirect = redirectUrl;
		}
		return redirect;
	}
}
