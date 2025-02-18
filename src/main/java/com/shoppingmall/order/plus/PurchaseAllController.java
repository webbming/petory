package com.shoppingmall.order.plus;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/order")
@Controller
public class PurchaseAllController {

@GetMapping("/index2")
public String index2() {
	return "order2/index";
}

@GetMapping("/delivery")
public String delivery() {
	return "order2/delivery";
}

@GetMapping("/orderOneItem")
public String orderOneItem() {
	return "order2/orderOneItem";
}
}
