package com.shoppingmall.faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqController {
	@GetMapping("/faq")
	public String faqPage() {
		return "faq/faq";
	}
}
