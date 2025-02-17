package com.shoppingmall.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


@Controller
public class PurchaseController {
	
	@Autowired
	PurchaseRepository repository;
	
	@Autowired
	PurchaseService service;
	
	@PostMapping("/order")
	public String orderList(@ModelAttribute Purchase purchase, Model model, @RequestParam(name = "reciver_addr_detail") String reciveaddr) {
		purchase.setReciverAddr(purchase.getReciverAddr() + " " + reciveaddr);
		Purchase savedPurchase = repository.save(purchase);
		model.addAttribute("purchase", savedPurchase);
	    return "order/order";
	}
	
	@PostMapping("/cancel")
	public String orderCancel(@RequestParam(name = "orderId") int id, Model model) {
		Optional<Purchase> purchase = service.orderCancel(id);
	    model.addAttribute(purchase.get());
		return "order/cancel";
	}
	
	@GetMapping("/orderAllList")
	public String orderAllList(Model model, @RequestParam(name = "buy", required = false) boolean buy) {
		List<Purchase> purchases = repository.findAll();
		 model.addAttribute("purchases", purchases); 
		System.out.println(purchases);
		 return "order/orderAllList";
	}
	
	//주문만 /취소된 목록만 확인
	@GetMapping("/orderList")
	public String orderList(Model model, @RequestParam(name = "cancel", required = false) Boolean cancel) {
		List<Purchase> purchases = repository.findAll();
		model.addAttribute("purchases", purchases); 
		if(cancel) {
			return "order/cancelList";
		}
		return "order/orderList";
	}
	
	@GetMapping("/orderListByUserid")
	public String orderListbyUserid(Model model, @RequestParam(name = "userId") String userId) {
		List<Purchase> purchases = repository.findByUserId(userId);
		System.out.println(purchases);
	    model.addAttribute("purchases", purchases);
	    return "order/orderAllList";  // Thymeleaf 템플릿으로 전달
	}
}