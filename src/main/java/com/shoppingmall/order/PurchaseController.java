package com.shoppingmall.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParsePosition;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class PurchaseController {
	
	@Autowired
	PurchaseService service;
	
	//주문
	@PostMapping("/order")
	public String orderList(@ModelAttribute Purchase purchase, Model model, @RequestParam(name = "reciver_addr_detail") String reciveaddr) {
		model.addAttribute("purchase", service.orderList(purchase, reciveaddr));
	    return "order/order";
	}
	
	//취소
	@PostMapping("/cancel")
	public String orderCancel(@RequestParam(name = "orderId") int id, Model model) {
		Optional<Purchase> purchase = service.orderCancel(id);
	    model.addAttribute(purchase.get());
		return "order/cancel";
	}
	
	//확인
	@GetMapping("/orderList")
	public String orderList(Model model, @RequestParam(name = "cancel", required = false) String cancel,@RequestParam(name = "user", required = false) String userId) {
		Map<String, Object> response;
		//전체확인
		if (userId == null || userId.isEmpty()) {
			response = service.orderAllList(cancel);
			model.addAttribute("title", response.get("title"));
			model.addAttribute("purchases", response.get("purchases"));
			return "order/orderAllList";
		}
		//특정 id별 확인
		else {
			System.out.println(userId);
			response = service.orderAllListByUserId(cancel, userId);
			System.out.println(response);
			model.addAttribute("title", response.get("title"));
			model.addAttribute("purchases", response.get("purchases"));
			model.addAttribute("userId", userId);
			return "order/orderAllListByUserId";
		}
	}
}