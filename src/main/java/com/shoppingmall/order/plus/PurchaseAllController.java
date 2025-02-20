package com.shoppingmall.order.plus;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.PurchaseList;
import com.shoppingmall.order.dto.PurchaseAllDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseItemRepository;
import com.shoppingmall.order.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
PurchaseDeliveryRepository delRepo;
@Autowired
PurchaseListRepository purRepo;
@Autowired
PurchaseItemRepository itemRepo;

@Autowired
PurchaseAllService service;

@GetMapping("/order")
public String order(@ModelAttribute PurchaseList purchase, @ModelAttribute PurchaseDelivery delivery,
					@ModelAttribute PurchaseItem item,
					@RequestParam(name = "reciver_addr_detail") String reciveDetailAddr,
					Model model){
	model.addAttribute("message", service.order(purchase, delivery, item, reciveDetailAddr));
	PurchaseAllDto purchaseAllDto = service.getOrderDetails(purchase.getPurchaseId());
	model.addAttribute("delivery", purchaseAllDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseAllDto.getPurchaseList());
	model.addAttribute("item", purchaseAllDto.getPurchaseItem());
	model.addAttribute("orderTime", purchaseAllDto);

	return "order2/orderResult";
}


	@PostMapping("/admin")
	public String purchaseListAll(Model model){
//		service.getOrderDetails();
		return "order/orderResultAll";
}
}
