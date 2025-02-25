package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.PurchaseDto;
import com.shoppingmall.order.service.PurchaseAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/order")
@Controller
public class PurchaseController {

@GetMapping
public String index() {
	return "order/index";
}

@GetMapping("/delivery")
public String delivery() {
	return "order/delivery";
}

@GetMapping("/orderOneItem")
public String orderOneItem() {
	return "order/orderOneItem";
}

@Autowired
PurchaseAllService service;

//주문 요청
@GetMapping("/order")
public String order(@ModelAttribute Purchase purchase, @ModelAttribute PurchaseDelivery delivery,
										@ModelAttribute PurchaseItem item,
										@RequestParam(name = "receiver_addr_detail") String receiveDetailAddr,
										Model model){
	model.addAttribute("message", service.order(purchase, delivery, item, receiveDetailAddr));
	PurchaseDto purchaseDto = service.getOrderDetails(purchase.getPurchaseId());
	model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseDto.getPurchase());
	model.addAttribute("item", purchaseDto.getPurchaseItem());

	return "order/orderResult";
}

	//주문번호 기준 주문검색
@GetMapping("/orders/{purchaseId}")
public String orderByPurchaseId(@PathVariable Long purchaseId, Model model){
	PurchaseDto purchaseDto = service.getOrderDetails(purchaseId);
	model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseDto.getPurchase());
	model.addAttribute("item", purchaseDto.getPurchaseItem());
	return "order/orderResult";
}

//전체 회원 리스트 주문 검색
@PostMapping("/admin/orderList")
public String orderAll(Model model){
		model.addAttribute("purchase", service.allList().getPurchase());
		model.addAttribute("delivery", service.allList().getPurchaseDelivery());
		model.addAttribute("item", service.allList().getPurchaseItem());
	return "order/orderResultAll";
	}

	//배송상태 변경
	@PostMapping("/admin/deiveryChange")
	public String deliveryChange(@RequestParam("deliveryState")String deliveryState,
								 @RequestParam("purchaseId")Long purchaseId,
										 Model model){
	model.addAttribute("message",service.deliveryChange(deliveryState, purchaseId));
	return "redirect:/order";
	}

	//주문 취소
	@PostMapping("/orders/{purchaseId}")
	public String orderCancel(@PathVariable Long purchaseId, Model model){
	model.addAttribute("message", service.purchaseCancel(purchaseId));
	return "redirect:/order";
	}

	//userId 기준 주문 검색
	@GetMapping("/orders/userId")
	public String orderListByUserId(@RequestParam(name = "userId") String userId
																	,@RequestParam(name = "orderState", required = false) String orderState, Model model){
	 PurchaseDto purchaseDto = service.orderListByUserId(userId, orderState);
		model.addAttribute("delivery", purchaseDto.getPurchaseDelivery());
		model.addAttribute("purchase", purchaseDto.getPurchase());
		model.addAttribute("item", purchaseDto.getPurchaseItem());
	return "order/orderListByUserId";
	}
}
