package com.shoppingmall.order.controller;

import com.shoppingmall.order.domain.PurchaseDelivery;
import com.shoppingmall.order.domain.PurchaseItem;
import com.shoppingmall.order.domain.Purchase;
import com.shoppingmall.order.dto.PurchaseAllDto;
import com.shoppingmall.order.repository.PurchaseDeliveryRepository;
import com.shoppingmall.order.repository.PurchaseItemRepository;
import com.shoppingmall.order.repository.PurchaseListRepository;
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

//주문번호 기준 주문검색
@GetMapping("/order")
public String order(@ModelAttribute Purchase purchase, @ModelAttribute PurchaseDelivery delivery,
										@ModelAttribute PurchaseItem item,
										@RequestParam(name = "receiver_addr_detail") String receiveDetailAddr,
										Model model){
	model.addAttribute("message", service.order(purchase, delivery, item, receiveDetailAddr));
	PurchaseAllDto purchaseAllDto = service.getOrderDetails(purchase.getPurchaseId());
	model.addAttribute("delivery", purchaseAllDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseAllDto.getPurchase());
	model.addAttribute("item", purchaseAllDto.getPurchaseItem());

	return "order/orderResult";
}

@GetMapping("/orders/{purchaseId}")
public String orderByPurchaseId(@PathVariable Long purchaseId, Model model){
	PurchaseAllDto purchaseAllDto = service.getOrderDetails(purchaseId);
	model.addAttribute("delivery", purchaseAllDto.getPurchaseDelivery());
	model.addAttribute("purchase", purchaseAllDto.getPurchase());
	model.addAttribute("item", purchaseAllDto.getPurchaseItem());
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
	return "order/index";
	}

	@PostMapping("/orders/{purchaseId}")
	public String orderCancel(@PathVariable Long purchaseId, Model model){
		System.out.println(purchaseId);
	service.orderCancel(purchaseId);
	model.addAttribute("message", "취소되었습니다");
	return "order/index";
	}

	//userId 기준 주문 검색
	@GetMapping("/orders/userId")
	public String orderListByUserId(@RequestParam(name = "userId") String userId, Model model){
	 PurchaseAllDto purchaseAllDto = service.orderListByUserId(userId);
		model.addAttribute("delivery", purchaseAllDto.getPurchaseDelivery());
		model.addAttribute("purchase", purchaseAllDto.getPurchase());
		model.addAttribute("item", purchaseAllDto.getPurchaseItem());
	return "order/orderListByUserId";
	}
}
