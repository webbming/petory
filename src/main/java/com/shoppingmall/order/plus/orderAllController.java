package com.shoppingmall.order.plus;

import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.repository.DeliveryRepository;
import com.shoppingmall.order.repository.OrderItemRepository;
import com.shoppingmall.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

@Autowired
DeliveryRepository delRepo;
@Autowired
OrderRepository purRepo;
@Autowired
OrderItemRepository itemRepo;

@Autowired
PurchaseAllService service;

@GetMapping("/order")
public String order(@ModelAttribute Purchase purchase, @ModelAttribute PurchaseDelivery delivery, @ModelAttribute PurchaseItem item, Model model){

	model.addAttribute("message", service.order(purchase, delivery, item));

	return "order/index";

}

}
